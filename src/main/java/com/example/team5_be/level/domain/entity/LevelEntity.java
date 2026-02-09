package com.example.team5_be.level.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //테이블을 의미, 이것만 쓰면 이름 HealthEntity인 테이블 만들어짐
@Table(name = "LEVEL_TBL") // @Entity랑 같이 쓰면 테이블 이름 바꿀 수 있음
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId ;
    
    private String levelName ;
    private Long levelDate ;
}
