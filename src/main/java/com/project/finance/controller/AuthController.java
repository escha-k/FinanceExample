package com.project.finance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    @PostMapping("/signup")
    public void signup() {

    }

    @PostMapping("/signin")
    public void signin() {

    }
}
