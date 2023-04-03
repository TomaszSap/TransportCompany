package com.example.TransportCompany.validations;

import com.example.TransportCompany.annotation.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator,String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );


    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext constraintValidatorContext)
    {
        Matcher matcher=PASSWORD_PATTERN.matcher(passwordField);
        return passwordField!=null &&(matcher.matches());
    }
}
