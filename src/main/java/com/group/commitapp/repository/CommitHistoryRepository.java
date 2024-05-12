package com.group.commitapp.repository;

import com.group.commitapp.domain.CommitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommitHistoryRepository extends JpaRepository<CommitHistory, Long> {

}
