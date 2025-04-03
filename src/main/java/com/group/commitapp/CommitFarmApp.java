package com.group.commitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommitFarmApp {

  public static void main(String[] args) {
    SpringApplication.run(CommitFarmApp.class, args);
  }

}
