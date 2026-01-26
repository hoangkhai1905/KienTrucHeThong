package com.example.spring_boot_api_jwt_ad.controller;

import com.example.spring_boot_api_jwt_ad.authen.UserPrincipal;
import com.example.spring_boot_api_jwt_ad.entity.Token;
import com.example.spring_boot_api_jwt_ad.entity.User;
import com.example.spring_boot_api_jwt_ad.service.TokenService;
import com.example.spring_boot_api_jwt_ad.service.UserService;
import com.example.spring_boot_api_jwt_ad.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){

        UserPrincipal userPrincipal =
                userService.findByUsername(user.getUsername());

        if (user == null || userPrincipal == null || !new BCryptPasswordEncoder()
                .matches(user.getPassword(), userPrincipal.getPassword())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account or password is not valid!");
        }

        // Access token
        String accessToken = jwtUtil.generateToken(userPrincipal);

        // Refresh token (random UUID stored server-side)
        String refreshToken = java.util.UUID.randomUUID().toString();

        // Persist access token
        Token access = new Token();
        access.setToken(accessToken);
        access.setTokenExpDate(jwtUtil.generateExpirationDate());
        access.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(access);

        // Persist refresh token with longer expiry (7 days)
        Token refresh = new Token();
        refresh.setToken(refreshToken);
        refresh.setTokenExpDate(new java.util.Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        refresh.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(refresh);

        java.util.Map<String, String> tokens = new java.util.HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }


    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity hello(){
        return ResponseEntity.ok("hello");
    }




//    Object principal = SecurityContextHolder
//            .getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//        UserPrincipal userPrincipal = (UserPrincipal) principal;
//    }

}
