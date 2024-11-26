package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime registerAt, modifyAt;
    private int commentCount;
    private int views;
    private int fileCount;

}
