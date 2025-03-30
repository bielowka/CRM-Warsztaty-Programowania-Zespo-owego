package com.customer.relationship.management.app.users;

import org.springframework.data.repository.Repository;

import java.util.*;

public interface UserRepository extends Repository<User, Long> {
    User save(User user);

    void delete(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void deleteAll();

    List<User> saveAll(Iterable<User> entities);

    Optional<User> findByEmail(String email);
}

class InMemoryUserRepository implements UserRepository {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.get(id) == null ? Optional.empty() : Optional.of(users.get(id));
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public List<User> saveAll(Iterable<User> users) {
        users.forEach(user -> this.users.put(user.getId(), user));
        List<User> savedUsers = new ArrayList<>();
        users.forEach(user -> savedUsers.add(this.users.get(user.getId())));
        return savedUsers;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail() == email)
                .findFirst();
    }

}