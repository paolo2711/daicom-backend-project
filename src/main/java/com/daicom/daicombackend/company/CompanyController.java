package com.daicom.daicombackend.company;

import com.daicom.daicombackend.company.dto.CompanyRequest;
import com.daicom.daicombackend.company.dto.CompanyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<CompanyResponse> get() {
        return ResponseEntity.ok(companyService.get());
    }

    // El frontend envía multipart/form-data (incluye posibles archivos que aquí se ignoran)
    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<CompanyResponse> update(@ModelAttribute CompanyRequest request) {
        return ResponseEntity.ok(companyService.update(request));
    }
}
