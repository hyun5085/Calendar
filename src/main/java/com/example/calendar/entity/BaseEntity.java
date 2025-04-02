package com.example.calendar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter // 클래스 내 모든 필드에 대해 자동으로 getter 메서드가 생성
@MappedSuperclass // 엔티티 클래스에서 공통된 필드를 상속받을 수 있도록 해주는 부모 클래스를 정의하는 데 사용
@EntityListeners(AuditingEntityListener.class)  // Entity Class추적 정보(생성일,수정일,생성자,수정자 등)를 자동으로 기록하는 기능
public abstract class BaseEntity {              // 추상 클래스는 필수적인 공통 기능을 정의할 때 사용

    @CreatedDate // 엔티티가 저장될 때 자동으로 생성 날짜 설정해주는 기능
    @Column(updatable = false)
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 여기에서는 자동 update 되지 않는 설정
    private LocalDateTime createdAt; // 생성 날짜

    @LastModifiedDate // 수정될 때마다 자동으로 마지막 수정일을 기록
    private LocalDateTime updatedAt; // 수정 날짜


}
