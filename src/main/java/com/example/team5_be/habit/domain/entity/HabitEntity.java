package com.example.team5_be.habit.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "habit")
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "style_id", nullable = false)
    private StyleEntity style;

    @Column(name = "habit_name", nullable = false, length = 100)
    private String name;

    @Column(name = "habit_definition", columnDefinition = "TEXT")
    private String definition;

    @Column(name = "start_value")
    private Integer startValue;

    @Column(name = "step_value")
    private Integer stepValue;

    @Column(name = "target_value")
    private Integer targetValue;

    @Column(name = "unit", length = 20)
    private String unit;
}
