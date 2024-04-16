package com.allancode.customer;

import com.allancode.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainersUnitTest {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void itShouldSelectAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();

    }

    @Test
    void itShouldSelectCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void itShouldReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();

    }

    @Test
    void itShouldInsertCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        underTest.insertCustomer(customer);

        //Then
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent();
    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);

        //When
        boolean exists = underTest.existsCustomerWithEmail(customer.getEmail());

        //Then
        assertThat(exists).isTrue();

    }

    @Test
    void itShouldReturnFalseWhenExistsCustomerWithEmailNotExist() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();


        //When
        boolean exists = underTest.existsCustomerWithEmail(email);

        //Then
        assertThat(exists).isFalse();

    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();

    }

    @Test
    void itShouldExistsCustomerWithId() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        boolean withId = underTest.existsCustomerWithId(id);

        //Then
        assertThat(withId).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenCustomerDoesNotExistWithId() {
        //Given
        int id = -1;

        //When
        boolean withId = underTest.existsCustomerWithId(id);

        //Then
        assertThat(withId).isFalse();

    }

    @Test
    void itShouldUpdateCustomerName() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent()
                .hasValueSatisfying(customer1 -> {
                    assertThat(customer1.getId()).isEqualTo(id);
                    assertThat(customer1.getName()).isEqualTo(newName);
                    assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(customer1.getAge()).isEqualTo(customer.getAge());
                });

    }

    @Test
    void itShouldUpdateCustomerEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent()
                .hasValueSatisfying(customer1 -> {
                    assertThat(customer1.getId()).isEqualTo(id);
                    assertThat(customer1.getName()).isEqualTo(customer.getName());
                    assertThat(customer1.getEmail()).isEqualTo(newEmail);
                    assertThat(customer1.getAge()).isEqualTo(customer.getAge());
                });


    }

    @Test
    void itShouldUpdateCustomerAge() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 30;

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent()
                .hasValueSatisfying(customer1 -> {
                    assertThat(customer1.getId()).isEqualTo(id);
                    assertThat(customer1.getName()).isEqualTo(customer.getName());
                    assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(customer1.getAge()).isEqualTo(newAge);
                });


    }

    @Test
    void itShouldUpdateAllCustomerDetails() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(22);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }
}