package org.example.web.config;


import jakarta.servlet.DispatcherType;
import org.example.web.Repo.CustomUserDetailsService;
import org.example.web.Repo.IProductRepository;
import org.example.web.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.example.web.Models.UserModel;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private IUserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private DataSource dataSource;


    @Autowired
    public SecurityConfig(DataSource dataSource) { this.dataSource = dataSource; }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize

                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()

                        .requestMatchers(HttpMethod.POST, "/register").permitAll()

                        .requestMatchers("/","/cart/**", "/register", "/login", "/catalog/**", "/contacts/**", "/category/**", "/categoryShow/**", "/images/**","/product/**", "/order/**", "/cart/orderConfirmation").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/profile", "/about", "/logout").hasAnyRole("USER", "ADMIN")
                        .requestMatchers( "/about", "/logout", "/profile").authenticated()

                        .anyRequest().authenticated())
                        .formLogin(form -> form
                        .loginPage("/login").permitAll())
                        .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true").permitAll());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}