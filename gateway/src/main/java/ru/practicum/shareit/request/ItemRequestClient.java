package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    private final String serverUrl;

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate);
        this.serverUrl = serverUrl;
    }

    // Создание запроса
    public ResponseEntity<Object> createRequest(Long userId, ItemRequestDto requestDto) {
        String path = serverUrl + API_PREFIX;
        return post(path, userId, requestDto);
    }

    // Получение запросов конкретного пользователя
    public ResponseEntity<Object> getUserRequests(Long userId) {
        String path = serverUrl + API_PREFIX;
        return get(path, userId);
    }

    // Получение всех запросов (постранично)
    public ResponseEntity<Object> getAllRequests(Long userId, Integer from, Integer size) {
        String path = serverUrl + API_PREFIX + "/all";
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        return get(path, userId, parameters);
    }

    // Получение конкретного запроса
    public ResponseEntity<Object> getRequest(Long requestId, Long userId) {
        String path = serverUrl + API_PREFIX + "/" + requestId;
        return get(path, userId);
    }
}
