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

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "merchants", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class MerchantEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false, nullable = false)
    private Long id;

    @Column(unique=true)
    @NotNull
    @Size(min = 5, message = "must be at least 5 characters")
    private String username = "";

    @NotNull
    @Column(unique=true)
    private String email = "";

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<MerchantWebsiteEntity> websites = new HashSet<>();

    protected MerchantEntity() {
    }

    public MerchantEntity(String username, String email) {
        this.username = username;
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

    public Set<MerchantWebsiteEntity> getWebsites() {
        return websites;
    }

    public void setWebsites(Set<MerchantWebsiteEntity> websites) {
        this.websites = websites;
    }
}
