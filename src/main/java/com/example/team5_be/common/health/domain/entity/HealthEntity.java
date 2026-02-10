package com.example.team5_be.common.health.domain.entity;

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
@Table(name = "health_tbl") // @Entity랑 같이 쓰면 테이블 이름 바꿀 수 있음
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthEntity {
    
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment 
    private Long id ;

    private String message ; // 일반 컬럼은 아무것도 안 붙임 

}
