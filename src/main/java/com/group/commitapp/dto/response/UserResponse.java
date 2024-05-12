package com.group.commitapp.dto.response;

import com.group.commitapp.domain.User;
import lombok.Getter;

@Getter
public class UserResponse {
  private long id;
  private String name;
  private Integer age;

  public UserResponse(long id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public UserResponse(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.age = user.getAge();
  }

  public UserResponse(long id, User user) {
    this.id = id;
    this.name = user.getName();
    this.age = user.getAge();
  }

}
