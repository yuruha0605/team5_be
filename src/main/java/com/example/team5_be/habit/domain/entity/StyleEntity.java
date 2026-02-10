package com.example.team5_be.habit.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "style")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StyleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_id")
    private Long styleId;

    @Column(name = "habit_style_name", nullable = false)
    private String habitStyleName;
}
