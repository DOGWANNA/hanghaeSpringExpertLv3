package com.sparta.hanghaespringexpertlv3.service;

import com.sparta.hanghaespringexpertlv3.dto.*;
import com.sparta.hanghaespringexpertlv3.entity.User;
import com.sparta.hanghaespringexpertlv3.entity.UserRoleEnum;
import com.sparta.hanghaespringexpertlv3.exception.DupilicateUserException;
import com.sparta.hanghaespringexpertlv3.exception.InvalidPasswordException;
import com.sparta.hanghaespringexpertlv3.exception.NotFoundUserException;
import com.sparta.hanghaespringexpertlv3.jwt.JwtUtil;
import com.sparta.hanghaespringexpertlv3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            throw new DupilicateUserException(); //등록 실패 시 예외 발생됨
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(username, password, role));

        return new ResponseEntity<>(new StatusResponseDto("가입 성공",200), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User foundUser = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                () -> new NotFoundUserException()
        );

        // 로그인 시 입력한 비밀번호와 찾아온 아이디의 비밀번호 비교
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), foundUser.getPassword())) {
            throw new InvalidPasswordException();
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(foundUser.getUsername(), foundUser.getRole()));

        return new ResponseEntity<>(new StatusResponseDto("로그인 성공",200), HttpStatus.OK);

    }
}