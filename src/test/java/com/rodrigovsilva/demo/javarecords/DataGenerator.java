package com.rodrigovsilva.demo.javarecords;

import com.rodrigovsilva.demo.javarecords.dto.Customer;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static Random random = new Random();

    private static Faker faker = new Faker(Locale.ENGLISH, random);

    public static Customer randomMerchant() {
        return new Customer(faker.name().name(), faker.internet().emailAddress());
    }

    public static Website randomWebsite() {
        return new Website(faker.internet().url(), faker.lorem().sentence());
    }


}
