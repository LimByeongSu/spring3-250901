package com.spring3.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/favicon.ico").permitAll()    //이건 그냥 그런게 있구나 하고 넘길것
                        .requestMatchers("/resource/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()  //DB쪽 접근이라 그냥 해두는게 맞는듯
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                //여기 적힌것 중 requestMatchers()에 있는 것은 허용하고
                //그 외의 것(anyRequest)은 막는 다(접근 권한이 없다고 뜸)는 의미다.
                //requestMatchers("/resource/**").permitAll() 은 resources/static/resource 안에 있는 내용은 다 허용한다.
                //그런데 위처럼 설정하면 이미지가 막히는데 resource

        ;
        return http.build();
    }
}
