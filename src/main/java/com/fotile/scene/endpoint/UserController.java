package com.fotile.scene.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {


    @PostMapping()
    public void addUser(){

    }

    @GetMapping("/{id}")
    public String findUser(@PathVariable("id")Long id){
        return "";
    }

    @PutMapping()
    public void updateUserName() {

        throw new RuntimeException("嘿嘿和");
    }


}
