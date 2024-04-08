package com.allancode;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
public class TestContainersTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("allancode-dao-unit-test")
            .withUsername("allancode")
            .withPassword("password");
    @Test
    void itShouldStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        //assertThat(postgreSQLContainer.isHealthy()).isTrue();

    }

    @Test
    void itShouldApplyDBMigrationsWithFlyway() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword())
                .load();
        flyway.migrate();
        System.out.println();

    }
}
