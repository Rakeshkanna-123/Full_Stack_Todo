package com.example.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
This class is created to stop the direct communication or request from the web to the control
layer. This class first filters the http request then decide whether to allow or not.
In simple it acts as the security layer between the web(https request) and the controller
 */

@Configuration // this annotation denotes that this class is declared for security configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() throws Exception{// this method automatically encodes the password
        return new BCryptPasswordEncoder();
    }

    @Bean //this a method level annotation to say that this method should be controlled by spring boot (IOC)
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception{
        System.out.println("Security config starts");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))//filtering the different server communication method which defined below
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/**").permitAll() //It allows every user to access authentication page
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("Security config ends");
        return http.build();
    }


    @Bean
    //this method ensures the connectivity betweem different allowed server (frontend)
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}