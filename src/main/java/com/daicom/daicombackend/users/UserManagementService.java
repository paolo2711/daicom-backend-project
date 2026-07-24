package com.daicom.daicombackend.users;

import com.daicom.daicombackend.auth.Role;
import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.roles.RoleRepository;
import com.daicom.daicombackend.users.dto.UserManagementRequest;
import com.daicom.daicombackend.users.dto.UserManagementResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, RoleRepository roleRepository,
                                 CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    // Mantiene el enum legacy `role` coherente con el rol granular asignado
    private Role deriveEnumRole(com.daicom.daicombackend.roles.Role roleEntity) {
        return (roleEntity != null && roleEntity.isAdmin()) ? Role.ADMIN : Role.USER;
    }

    public List<UserManagementResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserManagementResponse::new)
                .collect(Collectors.toList());
    }

    public UserManagementResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        return new UserManagementResponse(user);
    }

    public UserManagementResponse create(UserManagementRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso.");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        com.daicom.daicombackend.roles.Role roleEntity = resolveRole(request.getKind());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirst_name());
        user.setLastName(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(
                request.getPassword() != null ? request.getPassword() : ""));
        user.setRoleEntity(roleEntity);
        user.setRole(deriveEnumRole(roleEntity));
        user.setCompany(getMainCompany());

        return new UserManagementResponse(userRepository.save(user));
    }

    public UserManagementResponse update(Long id, UserManagementRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirst_name());
        user.setLastName(request.getLast_name());
        user.setEmail(request.getEmail());

        if (request.getKind() != null) {
            com.daicom.daicombackend.roles.Role roleEntity = resolveRole(request.getKind());
            user.setRoleEntity(roleEntity);
            user.setRole(deriveEnumRole(roleEntity));
        }

        // La contraseña sólo se cambia desde "Mi Perfil"; aquí se ignora si viene vacía
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return new UserManagementResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        userRepository.deleteById(id);
    }

    private com.daicom.daicombackend.roles.Role resolveRole(Long kind) {
        if (kind == null) return null;
        return roleRepository.findById(kind)
                .orElseThrow(() -> new RuntimeException("Rol asignado no encontrado."));
    }
}
