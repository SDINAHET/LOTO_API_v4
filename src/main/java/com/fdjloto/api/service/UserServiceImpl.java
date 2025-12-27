package com.fdjloto.api.service;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(UUID id) { // ✅ Garde UUID
        return userRepository.findById(id.toString());
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // @Override
    // public User updateUser(UUID id, User user) {
    //     Optional<User> existingUserOpt = userRepository.findById(id.toString());

    //     if (existingUserOpt.isPresent()) {
    //         User existingUser = existingUserOpt.get();

    //         // ⚠️ Ne pas modifier l'UUID
    //         user.setId(existingUser.getId());

    //         // ⚠️ Conserver les tickets existants si non fournis
    //         if (user.getTickets() == null || user.getTickets().isEmpty()) {
    //             user.setTickets(existingUser.getTickets());
    //         }

    //         return userRepository.save(user);
    //     }

    //     return null; // ou générer une exception pour un utilisateur introuvable
    // }
    @Override
    public User updateUser(UUID id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id.toString());

        if (existingUserOpt.isEmpty()) {
            // À toi de voir : RuntimeException, custom exception, etc.
            throw new RuntimeException("User not found with id: " + id);
        }

        User existingUser = existingUserOpt.get();

        // ✅ On met à jour UNIQUEMENT les infos de profil
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());

        // Le mot de passe est déjà encodé dans le controller
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(user.getPassword());
        }

        // ❌ NE PAS TOUCHER À :
        // - existingUser.setAdmin(...)
        // - existingUser.setTickets(...)
        // - existingUser.setId(...)
        // L’admin reste donc tel qu’il est en base.

        return userRepository.save(existingUser);  // @PreUpdate va gérer updatedAt
    }


    @Override
    public void deleteUser(UUID id) { // ✅ Garde UUID
        userRepository.deleteById(id.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // String role = user.getRole(); // Maintenant ROLE_ADMIN ou ROLE_USER
        // Utilisation du getter pour obtenir le rôle
        String role = user.getRole(); // ROLE_ADMIN ou ROLE_USER

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                // .roles(role.replace("ROLE_", "")) // Supprime le préfixe ROLE_ pour Spring Security
                // .roles(role)
                // .roles(user.getRole())
                .roles(role.replace("ROLE_", "")) // ⚠️ Supprime le préfixe ROLE_ pour Spring Security
                .build();
    }


}
