package com.daicom.daicombackend.clients;

import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;
import com.daicom.daicombackend.clients.dto.ClientRequest;
import com.daicom.daicombackend.clients.dto.ClientResponse;
import com.daicom.daicombackend.common.audit.AuditService;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final AuditService auditService;
    private final UserRepository userRepository;

    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository,
                          AuditService auditService, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.auditService = auditService;
        this.userRepository = userRepository;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    public List<ClientResponse> findAll() {
        return clientRepository.findByDeletedFalse().stream()
                .map(ClientResponse::new)
                .collect(Collectors.toList());
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado o eliminado."));
        return new ClientResponse(client);
    }

    public ClientResponse create(ClientRequest request, String currentUsername) {
        Client client = new Client();
        client.setName(request.getName());
        client.setDocumentType(request.getDocumentType());
        client.setDocument(request.getDocument() != null ? request.getDocument() : "");
        client.setAddress(request.getAddress());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());
        client.setCompany(getMainCompany());

        Client savedClient = clientRepository.save(client);

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        auditService.registrar(currentUser, "CREATE_CLIENT", "Client: " + savedClient.getName());

        return new ClientResponse(savedClient);
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado o eliminado."));
        
        client.setName(request.getName());
        client.setDocumentType(request.getDocumentType());
        client.setDocument(request.getDocument() != null ? request.getDocument() : "");
        client.setAddress(request.getAddress());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());

        Client updatedClient = clientRepository.save(client);
        return new ClientResponse(updatedClient);
    }

    public void delete(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado o eliminado."));
        client.setDeleted(true); // Soft delete
        clientRepository.save(client);
    }
}