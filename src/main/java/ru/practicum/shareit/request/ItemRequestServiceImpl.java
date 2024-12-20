package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemSummaryDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest createRequest(Long userId, ItemRequestDto requestDto) {

        User requestor = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    public List<ItemRequest> getUserRequests(Long userId) {
        return requestRepository.findByRequestorId(userId)
                .stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequest> getAllRequests(Long userId) {
        return requestRepository.findAll()
                .stream()
                .filter(request -> !request.getRequestor().getId().equals(userId))
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto getRequest(Long requestId, Long userId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        List<Item> items = itemRepository.findByRequest_Id(requestId);
        List<ItemSummaryDto> itemSummaries = items.stream()
                .map(item -> new ItemSummaryDto(item.getId(), item.getName(), item.getOwner().getId()))
                .toList();

        return new ItemRequestResponseDto(ItemRequestMapper.toItemRequestDto(request), itemSummaries);
    }
}
