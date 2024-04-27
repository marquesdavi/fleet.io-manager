package com.api.manager.fleet.domain.client;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Client(String name, String cpf, String address, String phone) {
        this.name = name;
        this.cpf = cpf;
        this.address = address;
        this.phone = phone;
    }
}

