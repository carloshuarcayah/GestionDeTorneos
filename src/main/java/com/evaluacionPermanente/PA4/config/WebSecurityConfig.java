package com.evaluacionPermanente.PA4.config;

import com.evaluacionPermanente.PA4.security.UserDetailsServiceImp;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .formLogin(form->form.loginPage("/login").permitAll())

                .authorizeHttpRequests(
                        //controlamos accesos
                        (auth)->auth
                                .requestMatchers("/karateca.jpg").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/academias/**","/karatecas/**","/llaves/**").authenticated()
                                .anyRequest().permitAll()
                )
                .userDetailsService(userDetailsServiceImp)
                .logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
                //controlar exp
                .exceptionHandling(customizer->customizer.accessDeniedHandler(accessDeniedHandlerApp()))
                //construimos con todas las configuraciones previas
                .build();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandlerApp(){
        return (
                (request, response, accessDeniedException) -> response.sendRedirect(request.getContextPath()+"/403")
                );
    }
}
