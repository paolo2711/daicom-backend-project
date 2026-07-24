package com.daicom.daicombackend.users;

import com.daicom.daicombackend.users.dto.UserManagementRequest;
import com.daicom.daicombackend.users.dto.UserManagementResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public ResponseEntity<List<UserManagementResponse>> getAll() {
        return ResponseEntity.ok(userManagementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserManagementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userManagementService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserManagementResponse> create(@Valid @RequestBody UserManagementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userManagementService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserManagementResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody UserManagementRequest request) {
        return ResponseEntity.ok(userManagementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
