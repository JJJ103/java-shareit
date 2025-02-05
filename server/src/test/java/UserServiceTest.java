import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Charlie", "charlie@example.com");
        userDto = new UserDto(1L, "Charlie", "charlie@example.com");
    }

    @Test
    void createUser_ShouldSaveUser() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void getUser_ShouldReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(user.getId());

        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void getUser_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(999L));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserDto updateDto = new UserDto(null, "Updated", "newemail@example.com");

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(99L, updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailAlreadyExists() {
        User existingUser = new User(2L, "Exist", "existing@example.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        UserDto updateDto = new UserDto(user.getId(), "ChangedName", "existing@example.com");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(user.getId(), updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ShouldUpdateOnlyName_WhenEmailIsNull() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updateDto = new UserDto(null, "NewName", null);
        User updated = userService.updateUser(user.getId(), updateDto);

        assertNotNull(updated);
        assertEquals("NewName", updated.getName());
        assertEquals(user.getEmail(), updated.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        when(userRepository.existsById(user.getId())).thenReturn(true);

        boolean result = userService.deleteUser(user.getId());
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
