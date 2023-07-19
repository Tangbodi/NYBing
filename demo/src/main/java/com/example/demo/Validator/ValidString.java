package com.example.demo.Validator;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidString {
    //Checks if password contains minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
    private static final String SPECIAL_CHARACTERS_REGEX = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W_]){1,})(?!.*\\s).{8,}$";
    private static final Integer SUBCATEGORY_MIN_VALUE = 101;
    private static final Integer SUBCATEGORY_MAX_VALUE = 620;
    private static final Integer USERNAME_MIN_LENGTH = 1;
    private static final Integer USERNAME_MAX_LENGTH = 18;
    private static final Integer PASSWORD_MIN_LENGTH = 8;
    private static final Integer PASSWORD_MAX_LENGTH = 30;
    private static final Integer POST_LENGTH = 36;
    private static final Integer EMAIL_MAX_LENGTH = 45;
    private static final Integer PHONE_LENGTH = 11;
    private static final Integer COMMENT_MIN_LENGTH = 1;
    private static final Integer TEXT_MIN_LENGTH = 1;
    private static final Integer TITLE_MIN_LENGTH = 1;
    private static final Integer TITLE_MAX_LENGTH = 255;



    //--------------------USERNAME--------------------
    public static boolean validUsername(String username){
        boolean hasWhitespace = username.contains(" ");
        boolean hasSpecialCharacters = !username.matches("[A-Za-z0-9 ]*");
        if (hasWhitespace || hasSpecialCharacters){
            return false;
        }else{
            return true;
        }
    }
    public static boolean UserNameLength(String username){
        if(username.length()>=USERNAME_MIN_LENGTH && username.length()<=USERNAME_MAX_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    public static boolean UserNameEmpty(String username){
        if(username.isEmpty() || username.isBlank() || username==null){
            return false;
        }else{
            return true;
        }
    }
    public static String fixName(String username){
        String fixedUsername = "";
        if(username.length()==USERNAME_MIN_LENGTH){
            fixedUsername = username.toUpperCase();
        }else if(username.length()>USERNAME_MIN_LENGTH){
            fixedUsername = username.substring(0,1).toUpperCase()+username.substring(1).toLowerCase();
        }else{
            return "";
        }
        return fixedUsername;
    }
    //--------------------EMAIL--------------------
    public static boolean EmailEmpty(String email){
        if(email.isEmpty() || email.isBlank() || email==null){
            return false;
        }else{
            return true;
        }
    }
    public static boolean validEmail(String email){
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
    public static boolean EmailLength(String email){
        if(email.length()>1 && email.length()<=EMAIL_MAX_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    //--------------------PASSWORD--------------------
    public static boolean PasswordEmpty(String password){
        if(password.isEmpty() || password.isBlank() || password==null){
            return false;
        }else{
            return true;
        }
    }
    public static boolean PasswordLength(String password){
        if(password.length() >= PASSWORD_MIN_LENGTH && password.length() <= PASSWORD_MAX_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    public static boolean validPassword(String password){
        Pattern pattern= Pattern.compile(SPECIAL_CHARACTERS_REGEX);
        Matcher matcher = pattern.matcher(password);
        boolean isPassword = matcher.find();
        return isPassword;
    }
    //------------------------PHONE NUMBER ------------------------
    public static boolean PhoneNumberEmpty(String phoneNumber){
        if(phoneNumber.isEmpty() || phoneNumber.isBlank() || phoneNumber==null){
            return false;
        }else{
            return true;
        }
    }
    public static boolean PhoneNumberLength(String phoneNumber){
        if(phoneNumber.length()==PHONE_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    public static boolean validPhoneNumber(String phoneNumber){
        String phoneRegex = "^(\\+\\d{1,3})?\\s?\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
           return true;
        } else {
            return false;
        }
    }
    //--------------------SUBCATEGORY--------------------
    public static boolean SubCategoryIdEmpty(Integer subCategoryId){
        if (subCategoryId == null){
            return false;
        }else {
            return true;
        }
    }
    public static boolean SubCategoryIdLength(Integer subCategoryId){
        if(subCategoryId>=SUBCATEGORY_MIN_VALUE && subCategoryId<=SUBCATEGORY_MAX_VALUE){
            return true;
        }else{
            return false;
        }
    }
    //--------------------POST--------------------
    public static boolean PostIdEmpty(String postId){
        if(postId==null || postId.isEmpty() || postId.isBlank()){
            return false;
        }else{
            return true;
        }
    }
    public static boolean validPostId(String postId){
        try {
            UUID uuid = UUID.fromString(postId);
            return true; // Valid UUID
        } catch (IllegalArgumentException e) {
            return false; // Invalid UUID
        }
    }
    public static boolean PostIdLength(String postId){
        if(postId.length()==POST_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    //--------------------COMMENT--------------------
    public static boolean CommentEmpty(String comment){
        if(comment==null || comment.isEmpty() || comment.isBlank()){
            return false;
        }else{
            return true;
        }
    }
    public static boolean CommentLength(String comment){
        if(comment.length()>=COMMENT_MIN_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    //--------------------TITLE--------------------
    public static boolean TitleEmpty(String title){
        if(title==null || title.isEmpty() || title.isBlank()){
            return false;
        }else{
            return true;
        }
    }
    public static boolean TitleLength(String title){
        if(title.length()>=TITLE_MIN_LENGTH && title.length()<TITLE_MAX_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    //--------------------TEXT--------------------
    public static boolean TextRenderEmpty(String textRender){
        if(textRender==null || textRender.isEmpty() || textRender.isBlank()){
            return false;
        }else{
            return true;
        }
    }
    public static boolean TextRenderLength(String textRender){
        if(textRender.length()>=TEXT_MIN_LENGTH){
            return true;
        }else{
            return false;
        }
    }
    //--------------------IMAGE--------------------
}
