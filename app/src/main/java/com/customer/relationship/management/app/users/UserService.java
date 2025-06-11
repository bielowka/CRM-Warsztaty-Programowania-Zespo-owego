package com.customer.relationship.management.app.users;

import com.customer.relationship.management.app.accounts.*;
import com.customer.relationship.management.app.sales.Sale;
import com.customer.relationship.management.app.sales.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final LeadRepository leadRepository;
    private final SaleRepository saleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AccountRepository accountRepository, LeadRepository leadRepository, SaleRepository saleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.leadRepository = leadRepository;
        this.saleRepository = saleRepository;
    }

    public User createUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public UserDTO updateUser(Long id, User userDetails) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existing.setFirstName(userDetails.getFirstName());
        existing.setLastName(userDetails.getLastName());
        existing.setEmail(userDetails.getEmail());
        existing.setPosition(userDetails.getPosition());
        existing.setActive(userDetails.isActive());
        existing.setRole(userDetails.getRole());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        User updated = userRepository.save(existing);
        return new UserDTO(updated);
    }



    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 1. Usuń sprzedaże powiązane z użytkownikiem
        List<Sale> sales = saleRepository.findBySalesRep(user);
        saleRepository.deleteAll(sales);

        // 2. Usuń leady i konta
        List<Account> accounts = accountRepository.findByUser(user);
        for (Account account : accounts) {
            List<Lead> leads = leadRepository.findByAccount(account);
            leadRepository.deleteAll(leads);
        }

        accountRepository.deleteAll(accounts);

        // 3. Usuń użytkownika
        userRepository.delete(user);
    }




    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void changeUserPassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
    }
}
