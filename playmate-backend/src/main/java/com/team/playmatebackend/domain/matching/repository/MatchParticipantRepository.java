package com.team.playmatebackend.domain.matching.repository;

import com.team.playmatebackend.domain.matching.entity.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
}
