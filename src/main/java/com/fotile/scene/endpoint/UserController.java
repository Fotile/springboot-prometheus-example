package com.fotile.scene.endpoint;

import com.fotile.scene.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void addUser(){
        userService.addUser();
    }

    @GetMapping("/{id}")
    public String findUser(@PathVariable("id")Long id){
        return "";
    }

    @PutMapping()
    public void updateUserName() {
        userService.updateUser();
        throw new RuntimeException("m3  good!!!!");
    }


}
