package com.group.commitapp.repository;

import com.group.commitapp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

  Optional<Users> findByName(String name);

}
