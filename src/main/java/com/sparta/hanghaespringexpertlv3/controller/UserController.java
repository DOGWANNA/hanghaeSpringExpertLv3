package com.sparta.hanghaespringexpertlv3.controller;

import com.sparta.hanghaespringexpertlv3.dto.LoginRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.LoginResponseDto;
import com.sparta.hanghaespringexpertlv3.dto.SignupRequestDto;
import com.sparta.hanghaespringexpertlv3.dto.SignupResponseDto;
import com.sparta.hanghaespringexpertlv3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new LoginResponseDto("로그인 성공","200");
    }

//    @ResponseBody
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
//        userService.login(loginRequestDto, response);
//        return "success";
//    }
}
