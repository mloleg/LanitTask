package ru.mloleg.lanittask.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.lanittask.AbstractTest;
import ru.mloleg.lanittask.dto.PersonWithCarsResponse;
import ru.mloleg.lanittask.dto.StatisticsResponse;
import ru.mloleg.lanittask.dto.common.CommonResponse;
import ru.mloleg.lanittask.model.Car;
import ru.mloleg.lanittask.model.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class AdminControllerTest extends AbstractTest {

    @BeforeEach
    public void setUp() {
        peopleRepository.saveAll(List.of(
                Person.builder().id(ID).name(NAME).birthdate(LocalDate.parse(BIRTHDATE, DateTimeFormatter.ofPattern("dd.MM.yyyy"))).build(),
                Person.builder().id(ID + 1).name(NAME + "2").birthdate(LocalDate.parse(BIRTHDATE, DateTimeFormatter.ofPattern("dd.MM.yyyy"))).build()
        ));

        carsRepository.saveAll(List.of(
                Car.builder().id(ID).model(MODEL).horsepower(HORSEPOWER).owner(peopleRepository.findById(ID).get()).build(),
                Car.builder().id(ID + 1).model(MODEL.toLowerCase(Locale.ROOT)).horsepower(HORSEPOWER + 100).owner(peopleRepository.findById(ID).get()).build(),
                Car.builder().id(ID + 2).model(MODEL.toUpperCase(Locale.ROOT)).horsepower(HORSEPOWER + 200).owner(peopleRepository.findById(ID + 1).get()).build(),
                Car.builder().id(ID + 3).model("BMW-X5").horsepower(HORSEPOWER + 300).owner(peopleRepository.findById(ID + 1).get()).build(),
                Car.builder().id(ID + 4).model("BmW-X6").horsepower(HORSEPOWER + 400).owner(peopleRepository.findById(ID + 1).get()).build()
        ));
    }

    @Test
    void when_getPersonWithCars_expect_success() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/admin/personwithcars?personid=" + ID))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().setCharacterEncoding("UTF-8");

        CommonResponse<PersonWithCarsResponse> response = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(ID, response.getBody().id());
        assertEquals(NAME, response.getBody().name());
        assertEquals(BIRTHDATE, response.getBody().birthdate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals(2, response.getBody().carList().size());
    }

    @Test
    void when_getPersonWithCars_withNonExistentPersonId_expect_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/admin/personwithcars?personid=" + 100))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void when_getStatistics_expect_success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/statistics"))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().setCharacterEncoding("UTF-8");

        CommonResponse<StatisticsResponse> response = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().peopleCount());
        assertEquals(5, response.getBody().carsCount());
        assertEquals(2, response.getBody().uniqueVendorsCount());
    }

    @Test
    void when_clear_expect_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/clear"))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(peopleRepository, Mockito.times(1)).deleteAll();
        Mockito.verify(carsRepository, Mockito.times(1)).deleteAll();
    }
}
