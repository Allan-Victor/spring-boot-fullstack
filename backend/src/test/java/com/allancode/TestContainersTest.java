package com.allancode;


import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestContainersTest extends AbstractTestContainersUnitTest {

    @Test
    void itShouldStartPostgresDb() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

    }
}
