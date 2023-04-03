package com.example.TransportCompany.security;

import com.example.TransportCompany.Mongo.AbstractMongoDao;
import com.example.TransportCompany.config.PasswordEncoderConfig;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.model.Role;
import com.example.TransportCompany.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PasswordProvider implements AuthenticationProvider {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoderConfig passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email= authentication.getName();
        String pwd=authentication.getCredentials().toString();
        Employee employee=employeeRepository.findByEmail(email);
        if(employee!=null && employee.getEmployeeId()>0 && passwordEncoder.matches(pwd,employee.getPwd()))
        {
            return new UsernamePasswordAuthenticationToken(email,null,getGrantedAuthorities(employee.getRole()));
        }else
            throw new BadCredentialsException("Invalid credentials!");
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Role role) {
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
