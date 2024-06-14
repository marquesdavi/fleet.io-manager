package com.api.manager.fleet.dto.customer;

import com.api.manager.fleet.util.annotation.pattern.PatternType;
import com.api.manager.fleet.util.annotation.pattern.PatternValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
public record CreateCustomerDTO(
        String name,
        @Email
        String email,
        @NotEmpty(message = "The CNPJ field can't be empty!")
        String cnpj,
        String phone
) {
}
