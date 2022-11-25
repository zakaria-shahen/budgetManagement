package com.tokyo.expensetracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Bean
    @Order(1)
    public DefaultSecurityFilterChain anyConfig(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .mvcMatcher("home")
                .authorizeHttpRequests().anyRequest().permitAll();

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public DefaultSecurityFilterChain userConfig(HttpSecurity httpSecurity, DbUserDetailsService dbUserDetailsService) throws Exception {

        httpSecurity
                // TODO: Turn on CSRf
                .csrf().disable()
                .httpBasic()
                .and().authorizeHttpRequests().anyRequest().authenticated()
                .and().userDetailsService(dbUserDetailsService);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}
