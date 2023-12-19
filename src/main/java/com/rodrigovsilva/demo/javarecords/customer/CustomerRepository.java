package com.rodrigovsilva.demo.javarecords.customer;


import com.rodrigovsilva.demo.javarecords.dto.Customer;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import com.rodrigovsilva.demo.javarecords.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByUsername(String name);

    // Records don't satisfy the JPA spec
    // JPA entities will remain tied to classes because they represent mutable state transitions

    // If you want immutable JPA entities:
    // make no setter methods, have public constructor that takes all properties, annotate with Hibernate's @Immutable

    // Records make great projections:
    // - Already mapped to DTO
    // - Result is immutable
    // - No lifecycle or managed state, so a query can be faster

    // this is a JPQL Constructor Expression
    // nested constructors are not allowed
    // collections in constructor ars are not allowed
    @Query("SELECT new com.rodrigovsilva.demo.javarecords.dto.Customer" +
            "(c.username, c.email) " +
            "FROM CustomerEntity c " +
            "ORDER BY c.username ASC ")
    List<Customer> loadCustomers();


    record CustomerDBRow(Long id, String username, String email) {
    }

    // Use $ instead of. as a class separator in qualified name for a constructor query
    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT new com.rodrigovsilva.demo.javarecords.customer.CustomerRepository$CustomerDBRow" +
            "(u.id, u.username, u.email)" +
            "FROM CustomerEntity u WHERE u.username=:username")
    Optional<CustomerDBRow> loadCustomerData(String username);


    @Query("SELECT new com.rodrigovsilva.demo.javarecords.dto.Website(web.url, web.description)" +
            "FROM WebsiteEntity web WHERE web.customer.username=:username")
    List<Website> loadCustomerWebsitesByUsername(String username);


    default Optional<Customer> findCustomers(String username) {

        var user = loadCustomerData(username);

        var addresses = user
                .map(u -> loadCustomerWebsitesByUsername(u.username()))
                .map(HashSet::new)
                .orElse(new HashSet<>());

        return user.map(u -> new Customer(u.username(), u.email(), addresses));
    }

}
