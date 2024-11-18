package com.jpmc.midascore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;

@RestController
public class BalanceController {
  
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/balance")
  public Balance getBalance(@RequestParam("userId") long userId) {
    UserRecord user = userRepository.findById(userId);

    if (user != null) {
      return new Balance(user.getId(), user.getBalance());
    }
    else {
      return new Balance(userId, 0);
    }
  }
}
