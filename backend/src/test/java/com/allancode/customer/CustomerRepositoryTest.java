package com.allancode.customer;

import com.allancode.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //loads anything that the jpa component needs to run
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {
    @Autowired
    private CustomerRepository underTest;




    @BeforeEach
    void setUp() {
    }

    @Test
    void itShouldExistsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        Integer id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();

    }
    @Test
    void itShouldReturnFalseWhenExistsCustomerWithEmailNotExist() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();


        //When
        var exists = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(exists).isFalse();

    }

    @Test
    void itShouldExistsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();


    }
    @Test
    void itShouldReturnFalseWhenCustomerDoesNotExistWithId() {
        //Given
        int id = -1;

        //When
        boolean withId = underTest.existsCustomerById(id);

        //Then
        assertThat(withId).isFalse();

    }

}