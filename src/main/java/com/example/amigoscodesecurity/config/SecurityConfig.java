package com.example.amigoscodesecurity.config;

import com.example.amigoscodesecurity.model.UserPermission;
import com.example.amigoscodesecurity.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.example.amigoscodesecurity.model.UserRole.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Cross Site Request Forgery. This should be used when application is used by browsers. Creates a token and expects it back when a get, post, or put request is received. It will match the og token to the one received with the req. If they match all is good, if not then a hacker tried to slip in a request while posing as the user
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                        .antMatchers("/api/*").hasRole(STUDENT.name())
//                        .antMatchers(HttpMethod.DELETE, "management/api/*").hasAuthority(UserPermission.COURSE_WRITE.getPermission()) // These 4 antMatchers are replaced my annotation role authentication
//                        .antMatchers(HttpMethod.POST, "management/api/*").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
//                        .antMatchers(HttpMethod.PUT, "management/api/*").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
//                        .antMatchers(HttpMethod.GET,"management/api/*").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())

                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
                .username("anna")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails lindaSmithUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())

                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.getGrantedAuthorities())

                .build();

        return new InMemoryUserDetailsManager(
                annaSmithUser,
                lindaSmithUser,
                tomUser
        );

    }
}
