package com.group.commitapp.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/gpt")
//@Api(value = "UserController", description = "User API")
public class GPTController {
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


}
