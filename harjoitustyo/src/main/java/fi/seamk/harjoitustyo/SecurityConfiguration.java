package fi.seamk.harjoitustyo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth

                // let all events by id be accessed, but not evetnts new
                
                
                .requestMatchers("/events/new").authenticated()
                
                .requestMatchers(
                    "/index.css", "/events.css", "/new.css", "/navbar.css", "/", "/events", "/categories", "/events/{id}").permitAll()
                    
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .permitAll() 
            )
            .logout(logout -> logout
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Create an in-memory user for testing purposes
        UserDetails user = User.builder()
                               .username("Palvelin")
                               .password(passwordEncoder.encode("Ohjelmointi"))
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
