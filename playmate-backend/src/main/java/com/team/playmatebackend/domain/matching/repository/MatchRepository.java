package com.team.playmatebackend.domain.matching.repository;

import com.team.playmatebackend.domain.matching.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
