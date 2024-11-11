package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Data
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor; // Пользователь, который запрашивает вещь

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created; // Дата и время создания запроса
}
