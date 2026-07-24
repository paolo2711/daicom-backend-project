package com.daicom.daicombackend.roles;

import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.roles.dto.PermissionAssignRequest;
import com.daicom.daicombackend.roles.dto.PermissionItem;
import com.daicom.daicombackend.roles.dto.RoleRequest;
import com.daicom.daicombackend.roles.dto.RoleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de RoleService con Mockito.
 * Verifica creación de roles, protección del rol admin y reemplazo de permisos.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock RoleRepository roleRepository;
    @Mock RolePermissionRepository rolePermissionRepository;
    @Mock CompanyRepository companyRepository;

    @InjectMocks RoleService roleService;

    @Test
    void create_guardaRolNoAdmin() {
        when(companyRepository.findAll()).thenReturn(List.of(new Company()));
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> {
            Role r = inv.getArgument(0);
            r.setId(5L);
            return r;
        });

        RoleRequest req = new RoleRequest();
        req.setName("Metrologo");

        RoleResponse resp = roleService.create(req);

        assertEquals("Metrologo", resp.getName());
        assertFalse(resp.isAdmin());
    }

    @Test
    void update_sobreRolAdministrador_lanzaExcepcion() {
        Role admin = new Role();
        admin.setName("Administrador");
        admin.setAdmin(true);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(admin));

        RoleRequest req = new RoleRequest();
        req.setName("Intento de cambio");

        assertThrows(RuntimeException.class, () -> roleService.update(1L, req));
    }

    @Test
    void delete_sobreRolAdministrador_lanzaExcepcion() {
        Role admin = new Role();
        admin.setAdmin(true);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThrows(RuntimeException.class, () -> roleService.delete(1L));
    }

    @Test
    void assignPermissions_reemplazaLosPermisosDelRol() {
        Role role = new Role();
        role.setId(2L);
        role.setName("Metrologo");
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));

        PermissionAssignRequest req = new PermissionAssignRequest();
        req.setRole(2L);
        req.setPermission_list(List.of(
                new PermissionItem(10, "Firmar / Generar QR", "FIRMAR_QR"),
                new PermissionItem(5, "Certificados", "/certificates")
        ));

        roleService.assignPermissions(req);

        // Primero borra los permisos previos, luego guarda los nuevos
        verify(rolePermissionRepository).deleteByRoleId(2L);
        verify(rolePermissionRepository, times(2)).save(any(RolePermission.class));
    }
}
