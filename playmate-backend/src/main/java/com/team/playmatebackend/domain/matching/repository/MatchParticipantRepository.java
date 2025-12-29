package com.team.playmatebackend.domain.matching.repository;

import com.team.playmatebackend.domain.matching.entity.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    Optional<MatchParticipant> findByMatchIdAndUserUserId(Long matchId, String userId);
}
