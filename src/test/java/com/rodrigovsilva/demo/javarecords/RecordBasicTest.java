package com.rodrigovsilva.demo.javarecords;

import com.rodrigovsilva.demo.javarecords.dto.Customer;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecordBasicTest {

    @Test
    public void testGeneratedMethods() {
        // using default constructors already defined in the record (auto-generated)
        var dataContainer1 = new Customer("demouser", "books@rodrigovsilvademo.com");
        var dataContainer2 = new Customer("demouser", "books@rodrigovsilvademo.com");

        // compare the two records using the accessor methods
        assertEquals(dataContainer1.username(), dataContainer2.username());
        assertEquals(dataContainer1.email(), dataContainer2.email());
        assertEquals(dataContainer1.websites(), dataContainer2.websites());

        // In Java records, equality is based on the data, not the reference
        assertEquals(dataContainer1, dataContainer2);

        // toString() and hashCode() are also auto-generated
        assertEquals(dataContainer1.toString(), dataContainer2.toString());
        assertEquals(dataContainer1.hashCode(), dataContainer2.hashCode());
    }

    @Test
    public void testInheritance() {
        assertEquals("java.lang.Record", Customer.class.getSuperclass().getName());
    }

    @Test
    public void testGenerics() {
        // records can be declared inline
        record DataContainer<T>(T value) {
        }

        // Here the generic type is inferred by the argument type.
        var container1 = new DataContainer<>(true);
        var container2 = new DataContainer<>("true");

        // note the return types of content()
        assertEquals(container1.value().toString(), container2.value());
    }

    @Test
    public void testConstructors() {
        // compact constructor is for validation logic
        assertThrows(IllegalArgumentException.class, () -> new Website((URI) null, ""));

        // test constructor for applying conversion and logic validation
        assertThrows(IllegalArgumentException.class, () -> new Website("https://www.example.com/path with space", ""));
    }

    @Test
    public void testImmutability() {

        var websites = new HashSet<Website>();
        websites.add(DataGenerator.randomWebsite());
        websites.add(DataGenerator.randomWebsite());
        websites.add(DataGenerator.randomWebsite());

        // Java records don't make the nested properties immutable by default
        Customer customer = new Customer("demo-customer", "books@test.com", websites);

        assertThrows(UnsupportedOperationException.class, () -> customer.websites().clear());
    }

    @Test
    public void testAnnotations() {

        var validator = Validation.buildDefaultValidatorFactory().getValidator();

        // annotations are applied on record components
        var invalidCustomer = new Customer("", "");

        // these will be applied on
        var violations = validator.validate(invalidCustomer);

        assertFalse(violations.isEmpty());
    }

    /**
     * This test demonstrates the use of records in a switch statement.
     *
     * @param <R>
     */
    sealed interface RequestResult<R> {

        // Define records for different types of products
        record Success<V>(V result) implements RequestResult<V> {
        }

        record Failure<V>(Throwable error) implements RequestResult<V> {
        }

        record Timeout<V>() implements RequestResult<V> {
        }
    }

    /**
     * This record represents the request body.
     *
     * @param value
     */
    record RequestBody(@NotBlank String value) {
    }

    /**
     * This class simulates an API requests and returns a response.
     */
    class RequestHandler {

        MyService service;

        public RequestHandler(MyService service) {
            this.service = new MyService();
        }

        public ResponseEntity<Boolean> handleRequest(RequestBody body) {
            return switch (validateRequest(body)) {
                case RequestResult.Success<Boolean> success -> ResponseEntity.ok(success.result());
                case RequestResult.Failure<Boolean> failure -> ResponseEntity.badRequest().build();
                case RequestResult.Timeout<Boolean> timeout -> ResponseEntity.status(504).build();
            };
        }

        private RequestResult<Boolean> validateRequest(RequestBody body) {
            try {
                service.processRequest(body);
                return new RequestResult.Success<>(true);
            } catch (SocketTimeoutException ste) {
                return new RequestResult.Timeout<>();
            } catch (Exception e) {
                return new RequestResult.Failure<>(e);
            }
        }
    }


    /**
     * This service class simulates a service that processes requests.
     */
    class MyService {

        public MyService() {
        }

        public String processRequest(@Valid RequestBody body) throws Exception {
            return switch (body.value()) {
                case "success" -> "request successful";
                case "failure" -> throw new Exception("failed");
                case "timeout" -> throw new SocketTimeoutException("timed out");
                default -> throw new IllegalArgumentException("invalid state");
            };
        }
    }


    @Test
    public void testPatternMatching() {

        var myService = new MyService();
        var handler = new RequestHandler(myService);

        // test success
        var success = handler.handleRequest(new RequestBody("success"));
        assertTrue(success.getStatusCode().is2xxSuccessful());

        // test failure
        var failure = handler.handleRequest(new RequestBody("failure"));
        assertTrue(failure.getStatusCode().is4xxClientError());

        // test timeout
        var timeout = handler.handleRequest(new RequestBody("timeout"));
        assertTrue(timeout.getStatusCode().is5xxServerError());

        // test null request
        var nullRequest = handler.handleRequest(null);
        assertTrue(nullRequest.getStatusCode().is4xxClientError());

    }
}
