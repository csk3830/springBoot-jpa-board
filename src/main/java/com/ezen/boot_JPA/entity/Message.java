package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Message extends TimeBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 200, nullable = false)
    private String receiver;


}
