package com.daicom.daicombackend.auth;

import com.daicom.daicombackend.auth.dto.AuthResponse;
import com.daicom.daicombackend.auth.dto.LoginRequest;
import com.daicom.daicombackend.auth.dto.ProfileResponse;
import com.daicom.daicombackend.auth.dto.RegisterRequest;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.roles.RolePermission;
import com.daicom.daicombackend.roles.RolePermissionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RolePermissionRepository rolePermissionRepository;

    public AuthService(UserRepository userRepository, CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       RolePermissionRepository rolePermissionRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    // Determina si el usuario es administrador (acceso irrestricto)
    private boolean isAdmin(User user) {
        if (user.getRoleEntity() != null && user.getRoleEntity().isAdmin()) return true;
        return user.getRole() == Role.ADMIN;
    }

    // Arma la respuesta de auth con kind (id de rol; 0 = admin) y los permisos concedidos
    private AuthResponse buildAuthResponse(User user, String token) {
        boolean admin = isAdmin(user);
        String roleName = admin ? "ADMIN" : "USER";

        Integer kind;
        List<Integer> permissions = new ArrayList<>();

        if (admin) {
            kind = 0; // kind < 1 → el frontend lo trata como acceso total
        } else if (user.getRoleEntity() != null) {
            kind = user.getRoleEntity().getId().intValue();
            permissions = rolePermissionRepository.findByRoleId(user.getRoleEntity().getId())
                    .stream().map(RolePermission::getPermissionCode).collect(Collectors.toList());
        } else {
            kind = -1; // usuario sin rol granular: sin permisos
        }

        return new AuthResponse(token, user.getUsername(), roleName, kind, permissions);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: El username ya está en uso.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso.");
        }

        // company principal (la crea el DataSeeder)
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            throw new RuntimeException("Error de sistema: No existe compañía registrada.");
        }
        Company mainCompany = companies.get(0);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        user.setCompany(mainCompany); // Vincula al único registro seed

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return buildAuthResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return buildAuthResponse(user, token);
    }

    public ProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        
        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}