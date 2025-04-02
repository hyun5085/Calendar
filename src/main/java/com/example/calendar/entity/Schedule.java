package com.example.calendar.entity;


import com.example.calendar.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
@Entity // JPA에서 데이터베이스의 테이블과 매핑되는 클래스
@Table(name = "schedule")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;                // 고유 식별자(ID)

    @Column(nullable = false)
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 내부 값이 없으면 안되는 설정만 가짐
    private String scheduleTitle;           // 일정 제목

    @Column(columnDefinition = "longtext")
    // 엔티티 클래스의 필드와 데이터베이스 테이블의 컬럼을 매핑하는 데 사용
    // 긴 내용을 쓸 수 있음.
    private String scheduleContent;          // 일정 내용

    @ManyToOne // 사용자가 다수의 일정과 연결이 될 수 있음
    @JoinColumn(name = "Consummer_consummerid") // 연결 < ConsummerId
    private Consummer consummer;


    public Schedule(){

    }

    public Schedule(String scheduleTitle, String scheduleContent){
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
    }

    public Schedule(ScheduleRequestDto scheduleRequestDto){
        this.scheduleTitle = scheduleRequestDto.getScheduleTitle();
        this.scheduleContent = scheduleRequestDto.getScheduleContent();
    }

    public void setConsummer(Consummer consummer){
        this.consummer = consummer;
    }

    public void update(String scheduleWriter, String scheduleTitle, String scheduleContent) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
    }



}
