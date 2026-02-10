package com.example.team5_be.habit.domain.entity;

import java.io.Serializable;
import java.util.Objects;

import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habit_relationship")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HabitRelationshipEntity {

    @EmbeddedId
    private HabitRelationshipId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @MapsId("habitId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habit_id", nullable = false)
    private HabitEntity habit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    /** 상태 변경은 의미 있게 */
    public void changeStatus(StatusEntity newStatus) {
        this.status = newStatus;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class HabitRelationshipId implements Serializable {

        @Column(name = "user_id", length = 255)
        private String userId;

        @Column(name = "habit_id")
        private Long habitId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HabitRelationshipId that = (HabitRelationshipId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(habitId, that.habitId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, habitId);
        }
    }
}
