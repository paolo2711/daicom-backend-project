package com.daicom.daicombackend.labs;

import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.labs.dto.LabRequest;
import com.daicom.daicombackend.labs.dto.LabResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabService {

    private final LabRepository labRepository;
    private final CompanyRepository companyRepository;

    public LabService(LabRepository labRepository, CompanyRepository companyRepository) {
        this.labRepository = labRepository;
        this.companyRepository = companyRepository;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    public List<LabResponse> findAll() {
        return labRepository.findAll().stream()
                .map(LabResponse::new)
                .collect(Collectors.toList());
    }

    public LabResponse findById(Long id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado."));
        return new LabResponse(lab);
    }

    public LabResponse create(LabRequest request) {
        Lab lab = new Lab();
        lab.setName(request.getName());
        lab.setCode(request.getCode());
        lab.setCompany(getMainCompany());
        
        Lab savedLab = labRepository.save(lab);
        return new LabResponse(savedLab);
    }

    public LabResponse update(Long id, LabRequest request) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado."));
        
        lab.setName(request.getName());
        lab.setCode(request.getCode());
        
        Lab updatedLab = labRepository.save(lab);
        return new LabResponse(updatedLab);
    }

    public void delete(Long id) {
        if (!labRepository.existsById(id)) {
            throw new RuntimeException("Laboratorio no encontrado.");
        }
        labRepository.deleteById(id); // Hard delete para Labs (no se exigió soft delete)
    }
}