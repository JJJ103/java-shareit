package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";
    private final String serverUrl;

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate);
        this.serverUrl = serverUrl;
    }

    // Создание вещи
    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        String path = serverUrl + API_PREFIX;
        return post(path, userId, itemDto);
    }

    // Обновление вещи
    public ResponseEntity<Object> updateItem(Long itemId, Long userId, ItemDto itemDto) {
        String path = serverUrl + API_PREFIX + "/" + itemId;
        return patch(path, userId, itemDto);
    }

    // Получение вещи
    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        String path = serverUrl + API_PREFIX + "/" + itemId;
        return get(path, userId);
    }

    // Получение всех вещей пользователя
    public ResponseEntity<Object> getAllItems(Long userId) {
        String path = serverUrl + API_PREFIX;
        return get(path, userId);
    }

    // Поиск вещей
    public ResponseEntity<Object> searchItems(String text) {
        String path = serverUrl + API_PREFIX + "/search";
        Map<String, Object> parameters = Map.of("text", text);
        return get(path, null, parameters);
    }

    // Добавление комментария
    public ResponseEntity<Object> addComment(Long itemId, Long userId, CommentDto commentDto) {
        String path = serverUrl + API_PREFIX + "/" + itemId + "/comment";
        return post(path, userId, commentDto);
    }
}
