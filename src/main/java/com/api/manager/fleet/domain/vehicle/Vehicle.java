package com.api.manager.fleet.domain.vehicle;

import com.api.manager.fleet.domain.client.Client;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Vehicle(String model, String brand, String plate, Integer year, Client client) {
        this.model = model;
        this.brand = brand;
        this.plate = plate;
        this.year = year;
        this.client = client;
    }
}


