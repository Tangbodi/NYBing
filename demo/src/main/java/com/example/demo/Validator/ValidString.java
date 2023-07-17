package com.example.demo.Validator;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidString {
    private static final String SPECIAL_CHARACTERS_REGEX = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";

    public static boolean verifyUserName(String username){
        boolean hasWhitespace = username.contains(" ");
        boolean hasSpecialCharacters = !username.matches("[A-Za-z0-9 ]*");

        if (hasWhitespace || hasSpecialCharacters){
            return true;
        }else{
            return false;
        }
    }
    public static boolean verifyEmail(String email){
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean verifyPassword(String password){
        Pattern pattern= Pattern.compile(SPECIAL_CHARACTERS_REGEX);
        boolean hasSpecialCharacters = pattern.matcher(password).find();
        boolean hasWhitespace = password.contains(" ");
        if(hasSpecialCharacters && !hasWhitespace){
            return true;
        }else{
            return false;
        }
    }
    public static boolean verifyPhoneNumber(String phoneNumber){
        String phoneRegex = "^(\\+\\d{1,3})?\\s?\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
           return true;
        } else {
            return false;
        }
    }
    public static String fixUsername(String username){
        String fixedUsername = "";
        if(username.length()==1){
            fixedUsername = username.toUpperCase();
        }else if(username.length()>1){
            fixedUsername = username.substring(0,1).toUpperCase()+username.substring(1).toLowerCase();
        }else{
            return "";
        }
       return fixedUsername;
    }
    public static boolean verifySubCategory(Integer subCategoryId){
        if(subCategoryId==null || subCategoryId > 620 || subCategoryId < 101){
            return false;
        }else{
            return true;
        }
    }
}
