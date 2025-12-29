package com.team.playmatebackend.domain.matching.repository;

import com.team.playmatebackend.domain.matching.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {

}
