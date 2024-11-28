package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MessageDTO;
import com.ezen.boot_JPA.entity.Message;
import com.ezen.boot_JPA.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<MessageDTO> getList(String name) {
        List<Message> messageList = messageRepository.findByReceiverOrderByRegisterAtDesc(name);
        if (!messageList.isEmpty()) {
            // 메시지 리스트를 DTO 리스트로 변환
            List<MessageDTO> messageDTOList = messageList.stream()
                    .map(this::convertEntityToDto)  // Message 객체를 MessageDTO 객체로 변환
                    .collect(Collectors.toList());  // 리스트로 모음
            return messageDTOList;  // 반환 타입이 List<MessageDTO>로 변경됨
        } else {
            return Collections.emptyList();  // 메시지가 없으면 빈 리스트 반환
        }
    }

    @Override
    public void delete(Long mno) {
        messageRepository.deleteById(mno);
    }

}
