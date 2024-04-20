package com.expctrl.dto.user;

import com.expctrl.domain.permission.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record RegisterDTO(
        @NotNull
        @NotBlank
        @Size(min = 8, max = 30)
        String email,
        @NotNull
        @NotBlank
        @Size(min = 8, max = 50)
        String password,
        String firstName,
        String lastName
) {
}
