package com.api.manager.fleet.domain.vehicle;

import com.api.manager.fleet.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "plate", nullable = false, unique = true)
    private String plate;

    @Column(name = "year", nullable = false)
    private Integer year;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Vehicle(String model, String brand, String plate, Integer year, Customer customer) {
        this.model = model;
        this.brand = brand;
        this.plate = plate;
        this.year = year;
        this.customer = customer;
    }
}


