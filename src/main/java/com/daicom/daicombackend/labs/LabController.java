package com.daicom.daicombackend.labs;

import com.daicom.daicombackend.labs.dto.LabRequest;
import com.daicom.daicombackend.labs.dto.LabResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labs")
public class LabController {

    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public ResponseEntity<List<LabResponse>> getAll() {
        return ResponseEntity.ok(labService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(labService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LabResponse> create(@Valid @RequestBody LabRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(labService.create(request, currentUsername));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResponse> update(@PathVariable Long id, @Valid @RequestBody LabRequest request) {
        return ResponseEntity.ok(labService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labService.delete(id);
        return ResponseEntity.noContent().build();
    }
}