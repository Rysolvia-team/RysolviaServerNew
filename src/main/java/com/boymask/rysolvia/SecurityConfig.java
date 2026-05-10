package com.boymask.rysolvia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            
            
//            http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/actuator/**").permitAll()
//                .anyRequest().authenticated()
//            )

            .authorizeHttpRequests(auth -> auth
                // API Android → LIBERE
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                // WEB → protetto
                .requestMatchers("/web/**").authenticated()

                .anyRequest().permitAll()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/web/home", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login")
            );

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails user = User.builder()
            .username("admin")
            .password(encoder.encode("2u3K7Pzr5Jp5RXB7zrq6ifs"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}