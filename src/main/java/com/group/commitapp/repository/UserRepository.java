package com.group.commitapp.repository;

import com.group.commitapp.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

	Optional<User> findByProviderId(String providerId);

	Optional<User> findByGithubId(String githubId);
}
