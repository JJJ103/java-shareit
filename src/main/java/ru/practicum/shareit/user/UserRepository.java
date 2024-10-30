package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private long userIdCounter = 1;

    public User createUser(User user) {
        user.setId(userIdCounter++);
        users.add(user);
        return user;
    }

    public User updateUser(Long id, User user) {
        User existingUser = findUserById(id);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            return existingUser;
        }
        return null;
    }

    public User getUser(Long id) {
        return findUserById(id);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }

    private User findUserById(Long id) {
        Optional<User> user = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        return user.orElse(null);
    }

    protected boolean existsByEmail(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
