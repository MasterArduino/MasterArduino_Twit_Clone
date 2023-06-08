package com.example.Sweater.config;

import com.example.Sweater.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserSevice userSevice; // нужен для того чтобы менеджер мог входить в базу
    // данных и искать пользователей и их роли


//!!!!!

    @Autowired
    private PasswordEncoder passwordEncoder;

    //"нужно в классе WebSecurityConfig сделать бин getPasswordEncoder() статическим.
    // Тогда по правилам java он будет подгружаться раньше,
    // чем конструктор и не будет никаких проблем.

    // spring.main.allow-circular-references=true Так ты позволяешь себе стрелять себе же в ногу.
    // Оно не зря по умолчанию false, в место лечения проблемы
    // ты предлагаешь просто закрыть глаза, то что советуют выше намного правильнее
    // с точки зрения программиста
    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
//    !!!!!!!!!!!!


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> {
                            try {
                                form
                                        .loginPage("/login")
                                        .permitAll()
                                        .and()
                                        .rememberMe();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
//        try {
//            form
//                    .loginPage("/login")
//                    .permitAll()
//                    .and()
//                    .rememberMe();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
                )

                .logout((logout) -> logout.permitAll());

        return http.build();
    }

//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userSevice)
//                .passwordEncoder(passwordEncoder());
//
//    }
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userSevice)
            .passwordEncoder(passwordEncoder); //пароль зашифрован при логине пользователя
}
   // https://stackoverflow.com/questions/49654143/spring-security-5-there-is-no-passwordencoder-mapped-for-the-id-null
//    @SuppressWarnings("deprecation")
//    @Bean
//    public static NoOpPasswordEncoder passwordEncoder() {
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }
}
