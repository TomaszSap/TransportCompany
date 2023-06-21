package com.example.TransportCompany.config;

import com.example.TransportCompany.security.PasswordProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PasswordProvider passwordProvider;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(passwordProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();/*ignoringAntMatchers("/home/**")
                .ignoringRequestMatchers(PathRequest.toH2Console()).and()
                .authorizeRequests()
                .mvcMatchers("/dashboard").authenticated()
                .mvcMatchers("/accountant/**").hasRole("ACCOUNTANT")
                .mvcMatchers("/forwarder/**").hasRole("FORWARDER")
                .mvcMatchers("/driver/**").hasRole("DRIVER")
                .mvcMatchers("/home").permitAll()
                .mvcMatchers("/admin/**").permitAll().hasRole("ADMIN")
                .mvcMatchers("/public/** ").permitAll()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true").permitAll()
                .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
                .and().httpBasic();
        http.headers().frameOptions().disable();*/
    }
}
