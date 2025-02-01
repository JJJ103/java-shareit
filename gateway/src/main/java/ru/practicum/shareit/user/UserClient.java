package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";
    private final String serverUrl;

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplate restTemplate) {
        super(restTemplate);
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        String path = serverUrl + API_PREFIX;
        return post(path, userDto);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserDto userDto) {
        String path = serverUrl + API_PREFIX + "/" + userId;
        return patch(path, userId, userDto);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        String path = serverUrl + API_PREFIX + "/" + userId;
        return get(path);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        String path = serverUrl + API_PREFIX + "/" + userId;
        return delete(path);
    }

    public ResponseEntity<Object> getAllUsers() {
        String path = serverUrl + API_PREFIX;
        return get(path);
    }
}