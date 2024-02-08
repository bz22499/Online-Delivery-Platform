package com.sep.onlinedeliverysystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends GlobalAuthenticationConfigurerAdapter {
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Qualifier("vendorDetailsService")
    @Autowired
    private UserDetailsService vendorDetailsService;

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
//                                .requestMatchers("/vendor/**").hasAuthority("VENDOR") //old
                                .requestMatchers("/profile/**").hasAuthority("USER")
                                .requestMatchers("/vendoritems/**", "/vendor/**").hasAuthority("VENDOR")
                                .anyRequest().permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home")
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
                .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder()));
    }

    private DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}