package com.rodrigovsilva.demo.javarecords.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false, nullable = false)
    private Long id;

    @Column(unique=true)
    @NotNull
    @Size(min = 3, message = "must be at least three characters")
    private String username = "";

    @NotNull
    @Column(unique=true)
    @Size(min = 3, message = "must be at least three characters")
    private String email = "";

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<WebsiteEntity> addresses = new HashSet<>();

    protected UserEntity() {
        // no arg constructor is required by JPA
    }

    public UserEntity(String name, String email) {
        this.username = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<WebsiteEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<WebsiteEntity> addresses) {
        this.addresses = addresses;
    }

}
