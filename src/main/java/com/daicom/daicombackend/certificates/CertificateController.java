package com.daicom.daicombackend.certificates;

import com.daicom.daicombackend.certificates.dto.CertificateRequest;
import com.daicom.daicombackend.certificates.dto.CertificateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<java.util.Map<String, Object>> getAll(
            @RequestParam(required = false) Long correlative) {
        
        List<CertificateResponse> list = certificateService.findAll();
        
        // Buscador 
        if (correlative != null) {
            list = list.stream()
                    .filter(c -> c.correlative != null && c.correlative.equals(correlative))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Devolver formato paginado { "count": X, "results": [...] }
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", list.size());
        response.put("results", list);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CertificateResponse> create(@Valid @ModelAttribute CertificateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(certificateService.create(request, currentUsername));
    }
    @GetMapping("/correlative")
    public ResponseEntity<java.util.Map<String, Long>> getNextCorrelative(@RequestParam("correlative_type") int correlativeType) {
        return ResponseEntity.ok(certificateService.getNextCorrelative(correlativeType));
    }

    //Desvincular, Anular y Vincular ---
    @PatchMapping("/{id}")
    public ResponseEntity<CertificateResponse> patch(@PathVariable Long id, @RequestBody java.util.Map<String, Object> updates) {
        return ResponseEntity.ok(certificateService.patch(id, updates));
    }

    // ---  edicionm ---
    @PutMapping("/{id}")
    public ResponseEntity<CertificateResponse> update(@PathVariable Long id, @ModelAttribute CertificateRequest request) {
        return ResponseEntity.ok(certificateService.update(id, request));
    }
}