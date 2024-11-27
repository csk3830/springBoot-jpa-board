package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MessageDTO;
import com.ezen.boot_JPA.entity.Message;
import com.ezen.boot_JPA.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Long insert(MessageDTO messageDTO) {
        return messageRepository.save(convertDtoToEntity(messageDTO)).getMno();
    }

    @Override
    public MessageDTO selectEmail(String name) {
        List<Message> messageList = messageRepository.findByReceiver(name);
        if (!messageList.isEmpty()) {
            // 첫 번째 메시지를 DTO로 변환
            MessageDTO messageDTO = convertEntityToDto(messageList.get(0));
            return messageDTO;
        } else {
            // 리스트가 비어있을 경우 처리할 로직
            return null;  // 메시지가 없을 때
        }
    }
}
