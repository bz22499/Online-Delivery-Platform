package com.sep.onlinedeliverysystem.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends GlobalAuthenticationConfigurerAdapter {
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Qualifier("vendorDetailsService")
    @Autowired
    private UserDetailsService vendorDetailsService;

    @Qualifier("driverDetailsService")
    @Autowired
    private UserDetailsService driverDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {    //Alternate between NoOp and BCrypt when you need to check passwords in the database
        return NoOpPasswordEncoder.getInstance(); //ONLY USE THIS WHEN YOU DON'T HAVE REAL USERS
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/profile/**", "/checkout/**").hasAuthority("USER")
                                .requestMatchers("/vendoritems/**", "/vendor/**", "/vendorProfile/**").hasAuthority("VENDOR")
                                .requestMatchers("/driverMain/**", "/driverProfile").hasAuthority("DRIVER")
                                .requestMatchers("/home").hasAnyAuthority("USER", "ROLE_ANONYMOUS") // Deny access to /home for vendors
                                .anyRequest().permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(successHandler())
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .authenticationProvider(authenticationProvider(vendorDetailsService, passwordEncoder()))
                .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder()))
                .authenticationProvider(authenticationProvider(driverDetailsService, passwordEncoder()));
    }

    private DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                if (roles.contains("VENDOR")) {
                    response.sendRedirect("/vendor");
                }else if (roles.contains("DRIVER")) {
                    response.sendRedirect("/driverMain");
                }else {
                    response.sendRedirect("/home");
                }
            }
        };
    }

    @Bean
    @Qualifier("customAuthenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}