package com.rodrigovsilva.demo.javarecords;

import com.rodrigovsilva.demo.javarecords.dto.Merchant;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecordBasicTest {
    
    @Test
    public void testGeneratedMethods() {
        // using default constructors already defined in the record (auto-generated)
        var dataContainer1 = new Merchant("demouser", "books@rodrigovsilvademo.com");
        var dataContainer2 = new Merchant("demouser", "books@rodrigovsilvademo.com");

        // compare the two records using the accessor methods
        assertEquals(dataContainer1.username(), dataContainer2.username());
        assertEquals(dataContainer1.email(), dataContainer2.email());

        // In Java records, equality is based on the data, not the reference
        assertEquals(dataContainer1, dataContainer2);

        assertEquals(dataContainer1.toString(), dataContainer2.toString());
    }

    @Test
    public void testInheritance() {
        assertEquals("java.lang.Record", Merchant.class.getSuperclass().getName());
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
        Merchant merchant = new Merchant("demouser", "books@rodrigovsilvademo.com", websites);

        assertThrows(UnsupportedOperationException.class, () -> merchant.websites().clear());
    }

    @Test
    public void testAnnotations() {

        var validator = Validation.buildDefaultValidatorFactory().getValidator();

        // annotations are applied on record components
        var invalidMerchant = new Merchant("", "");

        // these will be applied on
        var violations = validator.validate(invalidMerchant);

        assertFalse(violations.isEmpty());
    }

}
