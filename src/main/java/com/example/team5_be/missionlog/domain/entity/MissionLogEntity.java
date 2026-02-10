package com.example.team5_be.missionlog.domain.entity;

import java.time.LocalDate;

import com.example.team5_be.mission.domain.entity.MissionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "mission_log_tbl",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_mission_log",
        columnNames = {"mission_id", "check_date"}
    )
)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionlogId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private MissionEntity mission;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;
}
