package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private long mno;
    private String writer;
    private String content;
    private String receiver;
    private LocalDateTime registerAt;

}
