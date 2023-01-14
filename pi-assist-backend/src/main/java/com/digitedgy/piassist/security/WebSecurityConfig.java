package com.digitedgy.piassist.security;

import com.digitedgy.piassist.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();
            cors.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            cors.setAllowCredentials(true);
            return cors;
        });

        http.authorizeRequests().antMatchers("/static/css/**", "/static/js/**");
        http.cors().and().authorizeRequests().antMatchers("/pi-assist-server/**").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/user/**").hasAnyRole("Product-Owner","Team-Member","Scrum-Master","ADMIN");
        http.authorizeRequests().antMatchers("/users/**").hasAnyRole("Product-Owner","Team-Member","Scrum-Master","ADMIN");
        http.authorizeRequests().antMatchers("/refresh").hasAnyRole("Product-Owner","Scrum-Master","ADMIN");

        http.httpBasic().and().authorizeRequests().antMatchers("/").permitAll().and().csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
