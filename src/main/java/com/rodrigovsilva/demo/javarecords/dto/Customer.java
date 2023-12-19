package com.rodrigovsilva.demo.javarecords.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.Set;

public record Customer(@NotNull @Size(min = 5) String username, @NotNull @Email String email, Set<Website> websites) {

    public Customer(String username, String email) {
        this(username, email, Collections.emptySet()); // initialize with an immutable empty set
    }

    public Customer(String username, String email, Set<Website> websites) {
        this.username = username;
        this.email = email;
        this.websites = Collections.unmodifiableSet(websites); // to make the nested properties immutable as well
    }
}
