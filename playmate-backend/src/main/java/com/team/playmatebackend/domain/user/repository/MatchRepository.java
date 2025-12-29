package com.team.playmatebackend.domain.user.repository;

import com.team.playmatebackend.domain.user.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
