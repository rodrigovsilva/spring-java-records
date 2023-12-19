package com.rodrigovsilva.demo.javarecords.merchant;


import com.rodrigovsilva.demo.javarecords.dto.Merchant;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import com.rodrigovsilva.demo.javarecords.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    Optional<MerchantEntity> findByUsername(String name);

    // Records don't satisfy the JPA spec
    // JPA entities will remain tied to classes because they represent mutable state transitions

    // If you want immutable JPA entities:
    // make no setter methods, have public constructor that takes all properties, annotate with Hibernate's @Immutable

    // Records make great projections:
    // - Already mapped to DTO
    // - Result is immutable
    // - No lifecycle or managed state so query can be faster

    // this is a JPQL Constructor Expression
    // nested constructors are not allowed
    // collections in constructor ars are not allowed
    @Query("SELECT new com.rodrigovsilva.demo.javarecords.dto.Merchant" +
            "(m.username, m.email) " +
            "FROM MerchantEntity m " +
            "ORDER BY m.username ASC ")
    List<Merchant> loadMerchants();


    record MerchantDbRow(Long id, String username, String email) {
    }

    // Use $ instead of . as a class separator in qualified name for constructor query
    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT new com.rodrigovsilva.demo.javarecords.merchant.MerchantRepository$MerchantDbRow" +
            "(u.id, u.username, u.email)" +
            "FROM MerchantEntity u WHERE u.username=:username")
    Optional<MerchantDbRow> loadMerchantData(String username);


    @Query("SELECT new com.rodrigovsilva.demo.javarecords.dto.Website(web.url, web.description)" +
            "FROM MerchantWebsiteEntity web WHERE web.merchant.username=:username")
    List<Website> loadMerchantWebsitesByUsername(String username);


    default Optional<Merchant> findMerchant(String username) {

        var user = loadMerchantData(username);

        var addresses = user
                .map(u -> loadMerchantWebsitesByUsername(u.username()))
                .map(HashSet::new)
                .orElse(new HashSet<>());

        return user.map(u -> new Merchant(u.username(), u.email(), addresses));
    }

}
