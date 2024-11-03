package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private long userIdCounter = 1;

    public User createUser(User user) {
        user.setId(userIdCounter++);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(Long id, User user) {
        User existingUser = findUserById(id);
        if (existingUser != null) {
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
        }
        return existingUser;
    }

    public User getUser(Long id) {
        return findUserById(id);
    }

    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }

    private User findUserById(Long id) {
        return users.get(id);
    }

    protected boolean existsByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail() != null)
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
