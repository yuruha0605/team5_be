package com.example.team5_be.habit.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_id")
    private Long habitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "style_id", nullable = false)
    private StyleEntity style;

    @Column(name = "habit_name", nullable = false)
    private String habitName;

    @Column(name = "habit_definition")
    private String habitDefinition;

    @Column(name = "start_value")
    private Integer startValue;

    @Column(name = "step_value")
    private Integer stepValue;

    @Column(name = "target_value")
    private Integer targetValue;

    @Column(name = "unit")
    private String unit;

    @OneToMany(mappedBy = "habit", fetch = FetchType.LAZY)
    @Builder.Default
    private List<HabitRelationshipEntity> habitRelationships = new ArrayList<>();
}
