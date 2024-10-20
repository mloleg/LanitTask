package ru.mloleg.lanittask;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.mloleg.lanittask.repository.CarsRepository;
import ru.mloleg.lanittask.repository.PeopleRepository;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @SpyBean
    protected CarsRepository carsRepository;
    @SpyBean
    protected PeopleRepository peopleRepository;

    public static final Long ID = 1L;
    public static final String NAME = "John";
    public static final String BIRTHDATE = "01.01.2000";
    public static final String MODEL = "Mercedes-S4";
    public static final Integer HORSEPOWER = 200;

}
