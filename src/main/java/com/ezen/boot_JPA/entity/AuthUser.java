package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "auth_user") //이 이름으로 테이블명을 생성(쓰지 않으면 클래스명대로 테이블명 생성)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String auth;

}
