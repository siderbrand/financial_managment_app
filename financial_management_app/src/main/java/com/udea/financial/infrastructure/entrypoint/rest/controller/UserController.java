package com.udea.financial.infrastructure.entrypoint.rest.controller;

import com.udea.financial.domain.usecase.UserUseCase;
import com.udea.financial.infrastructure.entrypoint.rest.dto.UserRequestDTO;
import com.udea.financial.infrastructure.entrypoint.rest.dto.UserResponseDTO;
import com.udea.financial.infrastructure.entrypoint.rest.mapper.UserRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserUseCase userUseCase;
    private final UserRestMapper userRestMapper;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequestDTO request) {
        log.info("Creating user with email: {}", request.getEmail());
        userUseCase.saveUser(userRestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userRestMapper.toResponse(userUseCase.findUserById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userRestMapper.toResponse(userUseCase.findUserByEmail(email)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userUseCase.allUsers().stream()
                .map(userRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userUseCase.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
