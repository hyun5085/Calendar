package com.example.calendar.entity;

import com.example.calendar.dto.ConsummerRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
@Entity // JPA에서 데이터베이스의 테이블과 매핑되는 클래스
@Table(name = "consummer")
public class Consummer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consummerid; //

    @Column(nullable = false, unique = true)
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 내부 값이 없으면 안되고, 유일 값을 가지게 됨
    private String consummerName; // 사용자 이름;


    @Column(nullable = false)
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 내부 값이 없으면 안되는 설정만 가짐
    private String consummerPassword; // 사용자 비밀번호;


    @Column(nullable = false, unique = true)
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 유일 값을 가지게 됨
    private String consummerEmail;

    public Consummer() {

    }

    public Consummer(ConsummerRequestDto consummerRequestDto){
        this.consummerName = consummerRequestDto.getConsummerName();
        this.consummerPassword = consummerRequestDto.getConsummerPassword();
        this.consummerEmail = consummerRequestDto.getConsummerEmail();
    }

    public void updateConsummerPassword(String consummerPassword) {
        this.consummerPassword = consummerPassword;
    }
}
