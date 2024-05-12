package com.group.commitapp.repository;

import com.group.commitapp.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
