package com.udea.financial.infrastructure.entrypoint.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long idUser;
    private String name;
    private String email;
    private LocalDateTime registrationDate;
}
