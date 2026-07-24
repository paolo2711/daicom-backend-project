package com.daicom.daicombackend.clients;

import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;
import com.daicom.daicombackend.clients.dto.ClientRequest;
import com.daicom.daicombackend.clients.dto.ClientResponse;
import com.daicom.daicombackend.common.audit.AuditService;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de ClientService usando Mockito (sin base de datos real).
 * La lógica de negocio se prueba aislada, mockeando los DAO (repositorios).
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;
    @Mock CompanyRepository companyRepository;
    @Mock AuditService auditService;
    @Mock UserRepository userRepository;

    @InjectMocks ClientService clientService;

    @Test
    void create_guardaCliente_defaultDocumentoVacio_yRegistraAuditoria() {
        Company company = new Company();
        company.setName("DAICOM S.A.C.");
        when(companyRepository.findAll()).thenReturn(List.of(company));

        User user = new User();
        user.setUsername("admin1234");
        when(userRepository.findByUsername("admin1234")).thenReturn(Optional.of(user));

        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> {
            Client c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        ClientRequest req = new ClientRequest();
        req.setName("Cliente de Prueba");
        req.setDocumentType(DocumentType.NO_DOCUMENT);
        req.setDocument(null); // "SIN DOCUMENTO" -> debe quedar ""
        req.setEmail("prueba@daicom.com");

        ClientResponse resp = clientService.create(req, "admin1234");

        assertEquals("Cliente de Prueba", resp.getName());
        assertEquals("", resp.getDocument());                 // null se normaliza a ""
        assertEquals(3, resp.getDocumentType());              // NO_DOCUMENT -> 3
        assertEquals("SIN DOCUMENTO", resp.getDocumentType_name());
        verify(auditService).registrar(eq(user), eq("CREATE_CLIENT"), anyString());
    }

    @Test
    void create_conDni_conservaElNumeroDeDocumento() {
        when(companyRepository.findAll()).thenReturn(List.of(new Company()));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientRequest req = new ClientRequest();
        req.setName("Juan Perez");
        req.setDocumentType(DocumentType.DNI);
        req.setDocument("12345678");

        ClientResponse resp = clientService.create(req, "admin1234");

        assertEquals("12345678", resp.getDocument());
        assertEquals(1, resp.getDocumentType());              // DNI -> 1
    }
}
