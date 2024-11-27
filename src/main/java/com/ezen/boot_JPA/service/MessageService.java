package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MessageDTO;
import com.ezen.boot_JPA.entity.Message;

public interface MessageService {

    Long insert(MessageDTO messageDTO);

    default Message convertDtoToEntity(MessageDTO messageDTO) {
        return Message.builder()
                .mno(messageDTO.getMno())
                .title(messageDTO.getTitle())
                .writer(messageDTO.getWriter())
                .content(messageDTO.getContent())
                .receiver(messageDTO.getReceiver())
                .build();
    }

    default MessageDTO convertEntityToDto(Message message) {
        return MessageDTO.builder()
                .mno(message.getMno())
                .title(message.getTitle())
                .writer(message.getWriter())
                .content(message.getContent())
                .receiver(message.getReceiver())
                .registerAt(message.getRegisterAt())
                .build();
    }

    MessageDTO selectEmail(String name);
}
