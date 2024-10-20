package ru.mloleg.lanittask.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import ru.mloleg.lanittask.AbstractTest;
import ru.mloleg.lanittask.dto.CreateCarRequest;
import ru.mloleg.lanittask.dto.common.CommonRequest;
import ru.mloleg.lanittask.dto.common.CommonResponse;
import ru.mloleg.lanittask.dto.common.ValidationError;
import ru.mloleg.lanittask.model.Car;
import ru.mloleg.lanittask.model.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CarsControllerTest extends AbstractTest {

    @Test
    public void when_postCreate_expect_success() throws Exception {
        peopleRepository.save(Person.builder()
                .id(ID)
                .name(NAME)
                .birthdate(LocalDate.parse(BIRTHDATE, DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build());

        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .id(ID)
                .model(MODEL)
                .horsepower(HORSEPOWER)
                .ownerId(ID)
                .build();

        CommonRequest<CreateCarRequest> request = new CommonRequest<>(createCarRequest);

        mockMvc.perform(post("/api/v1/car")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(carsRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void when_postCreate_withExistingId_expect_badRequest() throws Exception {
        peopleRepository.save(Person.builder()
                .id(ID)
                .name(NAME)
                .birthdate(LocalDate.parse(BIRTHDATE, DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build());

        carsRepository.save(Car.builder()
                .id(ID)
                .model(MODEL)
                .horsepower(HORSEPOWER)
                .owner(peopleRepository.findById(ID).get())
                .build());

        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .id(ID)
                .model(MODEL)
                .horsepower(HORSEPOWER)
                .ownerId(ID)
                .build();

        CommonRequest<CreateCarRequest> request = new CommonRequest<>(createCarRequest);

        mockMvc.perform(post("/api/v1/car")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void when_postRequest_withJuvenilePerson_expect_badRequest() throws Exception {
        peopleRepository.save(Person.builder()
                .id(ID)
                .name(NAME)
                .birthdate(LocalDate.now().minusYears(15))
                .build());

        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .id(ID)
                .model(MODEL)
                .horsepower(HORSEPOWER)
                .ownerId(ID)
                .build();

        CommonRequest<CreateCarRequest> request = new CommonRequest<>(createCarRequest);

        mockMvc.perform(post("/api/v1/car")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void when_postCreate_withNonExistentPersonId_expect_notFound() throws Exception {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .id(ID)
                .model(MODEL)
                .horsepower(HORSEPOWER)
                .ownerId(10L)
                .build();

        CommonRequest<CreateCarRequest> request = new CommonRequest<>(createCarRequest);

        mockMvc.perform(post("/api/v1/car")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("postCreateInvalidRequestSource")
    void when_postCreate_withInvalidRequest_expect_validationError(
            CreateCarRequest createCarRequest, String validationErrorMessage) throws Exception {

        CommonRequest<CreateCarRequest> request = new CommonRequest<>(createCarRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/car")
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
                Arguments.of(new CreateCarRequest(null, MODEL, HORSEPOWER, ID), "ID машины не может быть null."),
                Arguments.of(new CreateCarRequest(0L, MODEL, HORSEPOWER, ID), "ID машины должен быть больше 0."),
                Arguments.of(new CreateCarRequest(-1L, MODEL, HORSEPOWER, ID), "ID машины должен быть больше 0."),
                Arguments.of(new CreateCarRequest(ID, null, HORSEPOWER, ID), "Марка машины не может быть пустой."),
                Arguments.of(new CreateCarRequest(ID, "", HORSEPOWER, ID), "Марка машины не может быть пустой."),
                Arguments.of(new CreateCarRequest(ID, "test", HORSEPOWER, ID), "Недопустимый формат марки машины."),
                Arguments.of(new CreateCarRequest(ID, "test--test", HORSEPOWER, ID), "Недопустимый формат марки машины."),
                Arguments.of(new CreateCarRequest(ID, MODEL, null, ID), "Мощность машины не может быть null."),
                Arguments.of(new CreateCarRequest(ID, MODEL, 0, ID), "Мощность машины должна быть больше 0."),
                Arguments.of(new CreateCarRequest(ID, MODEL, -1, ID), "Мощность машины должна быть больше 0."),
                Arguments.of(new CreateCarRequest(ID, MODEL, HORSEPOWER, null), "ID владельца машины не может быть null.")
        );
    }
}
