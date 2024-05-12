package com.group.commitapp.controller;

import com.group.commitapp.dto.request.UserCreateRequest;
import com.group.commitapp.dto.request.UserUpdateRequest;
import com.group.commitapp.dto.response.UserResponse;
import com.group.commitapp.service.UserServiceV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
//@Api(value = "UserController", description = "User API")
public class UserController {
  @Data
  @AllArgsConstructor
  static class Result<T>{
    private T data;
  }
  private final UserServiceV2 userService;
//  private final UserServiceV1 userService;

//  public UserController(UserServiceV2 userService) {
//    this.userService = userService;
//  }
  public UserController(UserServiceV2 userService) {
    this.userService = userService;
  }

  @PostMapping("/user") // POST /user
  public void saveUser(@RequestBody UserCreateRequest request) {
    userService.saveUser(request);
  }

  @GetMapping("/user")
  public List<UserResponse> getUsers() {
    return userService.getUsers();
  }

  @PutMapping("/user")
  public void updateUser(@RequestBody UserUpdateRequest request) {
    userService.updateUser(request);
  }

  @DeleteMapping("/user")
  public void deleteUser(@RequestParam String name) {
    userService.deleteUser(name);
  }

}
