package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MessageDTO;
import com.ezen.boot_JPA.entity.Message;

import java.util.List;

public interface MessageService {

    Long insert(MessageDTO messageDTO);

    default Message convertDtoToEntity(MessageDTO messageDTO) {
        return Message.builder()
                .mno(messageDTO.getMno())
                .writer(messageDTO.getWriter())
                .content(messageDTO.getContent())
                .receiver(messageDTO.getReceiver())
                .build();
    }

    default MessageDTO convertEntityToDto(Message message) {
        return MessageDTO.builder()
                .mno(message.getMno())
                .writer(message.getWriter())
                .content(message.getContent())
                .receiver(message.getReceiver())
                .registerAt(message.getRegisterAt())
                .build();
    }

    List<MessageDTO> getList(String name);

    void delete(Long mno);
}
