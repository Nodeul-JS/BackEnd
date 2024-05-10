package com.group.commitapp.user.controller;

import com.group.commitapp.user.dto.request.UserCreateRequest;
import com.group.commitapp.user.dto.request.UserUpdateRequest;
import com.group.commitapp.user.dto.response.UserResponse;
import com.group.commitapp.user.service.UserServiceV2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

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
