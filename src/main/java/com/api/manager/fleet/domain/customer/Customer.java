package com.api.manager.fleet.domain.customer;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    @CreationTimestamp
    private String createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private String updatedAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Customer(String name, String email, String cnpj, String phone) {
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
        this.phone = phone;
    }

    public Customer(CreateCustomerDTO customerDTO) {
        BeanUtils.copyProperties(this, customerDTO);
    }
}

