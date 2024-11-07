package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        if (userRepository.getUser(id) == null) {
            throw new UserNotFoundException();
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        return userRepository.updateUser(id, user);
    }

    @Override
    public User getUser(Long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.getUser(id) == null) {
            throw new UserNotFoundException();
        }
        return userRepository.deleteUser(id);
    }
}