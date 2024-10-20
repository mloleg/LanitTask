package ru.mloleg.lanittask.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.lanittask.AbstractTest;
import ru.mloleg.lanittask.dto.CreatePersonRequest;
import ru.mloleg.lanittask.dto.common.CommonRequest;
import ru.mloleg.lanittask.dto.common.CommonResponse;
import ru.mloleg.lanittask.dto.common.ValidationError;
import ru.mloleg.lanittask.model.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PeopleControllerTest extends AbstractTest {

    @Test
    public void when_postCreate_expect_success() throws Exception {
        CreatePersonRequest createPersonRequest = CreatePersonRequest.builder()
                .id(ID)
                .birthdate(BIRTHDATE)
                .name(NAME)
                .build();

        CommonRequest<CreatePersonRequest> request = new CommonRequest<>(createPersonRequest);

        mockMvc.perform(post("/api/v1/person")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(peopleRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void when_postCreate_withExistingId_expect_badRequest() throws Exception {
        peopleRepository.save(Person.builder()
                .id(ID)
                .birthdate(LocalDate.parse(BIRTHDATE, DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .name(NAME)
                .build());

        CreatePersonRequest createPersonRequest = CreatePersonRequest.builder()
                .id(ID)
                .birthdate(BIRTHDATE)
                .name(NAME)
                .build();

        CommonRequest<CreatePersonRequest> request = new CommonRequest<>(createPersonRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/person")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals(
                "ID {1} уже используется.",
                result.getResolvedException().getMessage());
    }

    @ParameterizedTest
    @MethodSource("postCreateInvalidRequestSource")
    void when_postCreate_withInvalidRequest_expect_validationError(
            CreatePersonRequest createPersonRequest, String validationErrorMessage) throws Exception {

        CommonRequest<CreatePersonRequest> request = new CommonRequest<>(createPersonRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/person")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        result.getResponse().setCharacterEncoding("UTF-8");

        CommonResponse<ValidationError> response = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals("Ошибка валидации", response.getErrorMessage());
        assertTrue(response.getValidationErrorList().stream()
                .anyMatch(e -> e.getMessage().equals(validationErrorMessage)));
    }

    public static Stream<Arguments> postCreateInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new CreatePersonRequest(null, NAME, BIRTHDATE), "ID человека не может быть null."),
                Arguments.of(new CreatePersonRequest(0L, NAME, BIRTHDATE), "ID человека должен быть больше 0."),
                Arguments.of(new CreatePersonRequest(-1L, NAME, BIRTHDATE), "ID человека должен быть больше 0."),
                Arguments.of(new CreatePersonRequest(ID, null, BIRTHDATE), "Имя человека не может быть пустым."),
                Arguments.of(new CreatePersonRequest(ID, "", BIRTHDATE), "Имя человека не может быть пустым."),
                Arguments.of(new CreatePersonRequest(ID, NAME, null), "Дата рождения человека не может быть null."),
                Arguments.of(new CreatePersonRequest(ID, NAME, ""), "Дата должна быть в прошлом и соответствовать формату \"дд.ММ.гггг\"."),
                Arguments.of(new CreatePersonRequest(ID, NAME, "test"), "Дата должна быть в прошлом и соответствовать формату \"дд.ММ.гггг\"."),
                Arguments.of(new CreatePersonRequest(ID, NAME, "01.01.2100"), "Дата должна быть в прошлом и соответствовать формату \"дд.ММ.гггг\".")
        );
    }
}
