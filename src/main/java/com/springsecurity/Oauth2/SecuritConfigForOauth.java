package com.springsecurity.Oauth2;

import com.springsecurity.Oauth2.filter.CustomOAuth2SuccessHandler;
import com.springsecurity.Oauth2.filter.OauthValidationFilter;
import com.springsecurity.Oauth2.util.OAuthTokenValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecuritConfigForOauth {

    @Autowired
    private OAuthTokenValidatorUtil tokenValidatorUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2SuccessHandler successHandler) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth -> oauth.successHandler(successHandler))
                .addFilterBefore(new OauthValidationFilter(tokenValidatorUtil), UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }

}
