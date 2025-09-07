//package com.springsecurity.JWT;
//
//import com.springsecurity.JWT.authenticationProviders.JWTAuthenticationProvider;
//import com.springsecurity.JWT.filter.JWTAuthenticationFilter;
//import com.springsecurity.JWT.filter.JWTRefreshFilter;
//import com.springsecurity.JWT.filter.JwtValidationFilter;
//import com.springsecurity.JWT.util.JWTUtil;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.util.Arrays;
//
////@Configuration
////@EnableWebSecurity
//public class SecurityConfigForJWT {
//
//    private JWTUtil jwtUtil;
//    private UserDetailsService userDetailsService;
//
//    public SecurityConfigForJWT(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
//        this.userDetailsService = userDetailsService;
//        this.jwtUtil = jwtUtil;
//    }
//
////    @Bean
//    public JWTAuthenticationProvider jwtAuthenticationProvider(){
//        return new JWTAuthenticationProvider(jwtUtil,userDetailsService);
//    }
//
////    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return  provider;
//    }
//
////    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
//                                                   JWTUtil jwtUtil) throws Exception {
//
//        // Authentication filter responsible for login
//        JWTAuthenticationFilter jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);
//
//        // Validation filter for checking JWT in every request
//        JwtValidationFilter jwtValidationFilter = new JwtValidationFilter(authenticationManager);
//
//        // refresh filter for checking JWT in every request
//        JWTRefreshFilter jwtRefreshFilter = new JWTRefreshFilter(jwtUtil, authenticationManager);
//
//        http.authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/user-register").permitAll()
//                        .requestMatchers("/api/users").hasRole("USER")
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(csrf -> csrf.disable())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // generate token filter
//                .addFilterAfter(jwtValidationFilter, JWTAuthenticationFilter.class) // validate token filter
//                .addFilterAfter(jwtRefreshFilter, JwtValidationFilter.class); // refresh token filter
//        return http.build();
//    }
//
////    @Bean
//    public AuthenticationManager authenticationManager() {
//        return new ProviderManager(Arrays.asList(
//                daoAuthenticationProvider(),
//                jwtAuthenticationProvider()
//        ));
//    }
//
//
//
//}
