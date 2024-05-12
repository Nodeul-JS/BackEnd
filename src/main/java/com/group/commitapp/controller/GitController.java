package com.group.commitapp.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/git")
//@Api(value = "UserController", description = "Git API")
public class GitController {
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    //@ApiOperation(value = "당일 커밋 조회 ", notes = "/api/git/{userId}")
    //@GetMapping("/{userId}")

    //@ApiOperation(value = "스트릭 지급 ", notes = "/api/git/update/freeze/{userId}")
    //@PutMapping("/update/freeze/{userId}")




}
