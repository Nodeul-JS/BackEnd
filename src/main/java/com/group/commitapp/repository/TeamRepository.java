package com.group.commitapp.repository;

import com.group.commitapp.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
