//package com.springsecurity.JWT.controller;
//
//import com.springsecurity.JWT.entity.UserRegisterEntity;
//import com.springsecurity.JWT.service.UserRegisterEntityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class UserController {
//
//    @Autowired
//    UserRegisterEntityService userRegisterEntityService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @PostMapping("/user-register")
//    public ResponseEntity<String> register(@RequestBody UserRegisterEntity userRegisterDetaisl){
//        // Hash the Password before saving
//
//        userRegisterDetaisl.setPassword(passwordEncoder.encode(userRegisterDetaisl.getPassword()));
//
//        userRegisterEntityService.save(userRegisterDetaisl);
//        return ResponseEntity.ok("User register successfully");
//    }
//
//    @GetMapping("/users")
//    public String getUsersDetails(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return "Fetch user detail successfully";
//    }
//}
