package com.daicom.daicombackend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de seguridad (integración): verifica que la capa de autenticación JWT
 * protege los endpoints privados y deja públicos los permitidos.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void endpointProtegido_sinToken_esRechazado() {
        ResponseEntity<String> resp = rest.getForEntity("/clients", String.class);
        // Sin token JWT, Spring Security debe rechazar (401 o 403)
        assertTrue(resp.getStatusCode().is4xxClientError(),
                "Un endpoint protegido sin token debe responder 4xx, respondió: " + resp.getStatusCode());
    }

    @Test
    void loginConCredencialesInvalidas_esRechazado() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> body = new HttpEntity<>(
                "{\"username\":\"noexiste\",\"password\":\"malo\"}", headers);

        ResponseEntity<String> resp = rest.postForEntity("/auth/login", body, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    void actuatorHealth_esPublico() {
        ResponseEntity<String> resp = rest.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
