package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class AccountService {

    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;

    public List<AccountInfo> findAll() {
        return accountRepository.findAllBy();
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(CreateAccountDTO createAccountDTO, User user) {
        Account account = new Account();
        account.setUser(user);

        account.setFirstName(createAccountDTO.getFirstName());
        account.setLastName(createAccountDTO.getLastName());
        account.setEmail(createAccountDTO.getEmail());
        account.setAccountStatus(createAccountDTO.getAccountStatus());
        account.setPhoneNumber(createAccountDTO.getPhoneNumber());
        if (createAccountDTO.getCompanyName() != null || createAccountDTO.getIndustry() != null) {
            Company company = new Company();
            company.setName(createAccountDTO.getCompanyName());
            company.setIndustry(createAccountDTO.getIndustry());
            account.setCompany(company);
        }
        return accountRepository.save(account);
    }

    public Account updateAccount(Account existing, @Valid UpdateAccountDTO updateAccountDTO) {
        existing.setFirstName(updateAccountDTO.getFirstName());
        existing.setLastName(updateAccountDTO.getLastName());
        existing.setEmail(updateAccountDTO.getEmail());
        existing.setPhoneNumber(updateAccountDTO.getPhoneNumber());
        existing.setAccountStatus(updateAccountDTO.getAccountStatus());
        if (updateAccountDTO.getCompanyName() != null || updateAccountDTO.getIndustry() != null) {
            companyRepository.findByNameAndIndustry(updateAccountDTO.getCompanyName(), updateAccountDTO.getIndustry()).ifPresent(
                    existing::setCompany
            );
        }
        return accountRepository.save(existing);
    }

    public Account save(Account account) {
        if (account.getUser() == null) {
            throw new IllegalArgumentException("Account must have an associated user");
        }
        return accountRepository.save(account);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public List<AccountInfo> findAllByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public List<AccountInfo> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId) {
        return accountRepository.findAllByAccountStatusAndUserId(accountStatus, userId);
    }

    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }
}