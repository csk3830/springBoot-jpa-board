package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

/* Entity : DB의 테이블 클래스 
*  DTO : 객체를 생성하는 클래스
*  자주 쓰는 코드들 : base class로 별도 관리
*  regDate / modDate / isDel
* 
*  Id = 기본키
*  기본키 생성 전략 GeneratedValue
* */

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board extends TimeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increments 생성
    private long bno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = false)
    private String content;

    // 생성시 초기화 값을 지정 : 객체가 생길 때 객체의 기본값 생성
    // @Builder.Default
    // private int point = 0;

    private int commentCount;
    private int views;
    private int fileCount;
}
