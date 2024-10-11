package com.example.springboottutorial;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // To use H2 or MySQL profile if needed
@ActiveProfiles("h2")  // Use the H2 profile
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        // Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        // Verify the saved customer has an ID assigned
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getFirstName()).isEqualTo("John");
        assertThat(savedCustomer.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testFindById() {
        // Create and save a new customer
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer = customerRepository.save(customer);

        // Find the customer by ID
        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());

        // Verify the customer exists
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getFirstName()).isEqualTo("Jane");
        assertThat(foundCustomer.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    public void testFindCustomerById() {
        // Create and save a customer
        Customer customer = new Customer();
        customer.setFirstName("Alice");
        customer.setLastName("Wonderland");
        customerRepository.save(customer);

        // Use the custom repository method
        Customer foundCustomer = customerRepository.findCustomerById(customer.getId());

        // Verify the customer was found
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("Alice");
        assertThat(foundCustomer.getLastName()).isEqualTo("Wonderland");
    }

    @Test
    public void testFindAllCustomers() {
        // Save multiple customers
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");

        Customer customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");



        // Retrieve all customers
        Iterable<Customer> customers = customerRepository.findAll();
        List<Customer> customerList = StreamSupport.stream(customers.spliterator(), false)
                .collect(Collectors.toList());
        int oldRecordCount = customerList.size();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // Verify the number of customers
        // Retrieve all customers
        Iterable<Customer> customersNow = customerRepository.findAll();
        assertThat(customersNow).hasSize(oldRecordCount+2);
    }
}
