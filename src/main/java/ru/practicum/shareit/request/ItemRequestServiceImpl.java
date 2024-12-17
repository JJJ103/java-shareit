package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto) {
        User requestor = userRepository.findById(requestDto.getRequestorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ItemRequestMapper.toDto(requestRepository.save(ItemRequestMapper.toEntity(requestDto, requestor)));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        return requestRepository.findByRequestorId(userId).stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        return requestRepository.findAll().stream()
                .filter(request -> !request.getRequestor().getId().equals(userId))
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(Long requestId, Long userId) {
        return requestRepository.findById(requestId)
                .map(ItemRequestMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
    }
}
