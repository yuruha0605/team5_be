package com.example.team5_be.habit.domain.entity;

import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "habit_relationship")
public class HabitRelationshipEntity {

    @EmbeddedId
    private HabitRelationshipId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private UserEntity member;

    @MapsId("habitId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habit_id", nullable = false)
    private HabitEntity habit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Embeddable
    public static class HabitRelationshipId implements Serializable {

        @Column(name = "member_id")
        private Long memberId;

        @Column(name = "habit_id")
        private Long habitId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HabitRelationshipId that = (HabitRelationshipId) o;
            return Objects.equals(memberId, that.memberId)
                    && Objects.equals(habitId, that.habitId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberId, habitId);
        }
    }
}
