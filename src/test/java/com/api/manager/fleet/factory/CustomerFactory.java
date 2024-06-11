package com.api.manager.fleet.factory;

import com.api.manager.fleet.domain.customer.Customer;
import com.github.javafaker.Faker;

import java.util.Random;

public class CustomerFactory {
    public static Customer get(){
        Faker faker = new Faker();

        long leftLimit = 1L;
        long rightLimit = 100000L;

        return Customer.builder()
                .id(leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                .name(faker.name().fullName())
                .email(faker.bothify("????##@email.com"))
                .cnpj(faker.numerify("##############"))
                .phone(faker.numerify("###########"))
                .build();
    }
}
