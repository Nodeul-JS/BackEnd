package com.group.commitapp.service;

import com.group.commitapp.domain.User;
import com.group.commitapp.dto.request.UserCreateRequest;
import com.group.commitapp.dto.request.UserUpdateRequest;
import com.group.commitapp.dto.response.UserResponse;
import com.group.commitapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

  private final UserRepository userRepository;

  public UserServiceV2(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 아래 있는 함수가 시작될 때 start transaction;을 해준다 (트랜잭션을 시작!)
  // 함수가 예외 없이 잘 끝났다면 commit
  // 혹시라도 문제가 있다면 rollback
  @Transactional
  public void saveUser(UserCreateRequest request) {
    userRepository.save(new gitUser(request.getName(), request.getAge()));
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream()
        .map(UserResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUser(UserUpdateRequest request) {
    User user = userRepository.findById(request.getId())
        .orElseThrow(IllegalArgumentException::new);
    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    User user = userRepository.findByName(name)
        .orElseThrow(IllegalArgumentException::new);

    userRepository.delete(user);
  }


}
