package com.ivs.user_service.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*_+=-`~";

    public AuthenticationService() {
    }
	
    public String hashPassword(String saltValue, String rawPassword) {
        String passwordWithSalt = saltValue + rawPassword;

        // SHA2HASH15
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(passwordWithSalt.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not available", e);
        }

    }


    public PasswordDetails makePassword(String rawPassword) {
        StringBuilder randomStringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = (int) (characters.length() * Math.random());
            randomStringBuilder.append(characters.charAt(index));
        }

        String saltValue = randomStringBuilder.toString();
        String hashedPassword = hashPassword(saltValue, rawPassword);
        return new PasswordDetails(saltValue, hashedPassword);
    }
    
    
    public static class PasswordDetails {
    	String saltValue = "";
    	String hashedPassword = "";
    	
		PasswordDetails(String saltValue, String hashedPassword) {
			this.saltValue = saltValue;
			this.hashedPassword = hashedPassword;
		}
		
		public String getSaltValue() {
			return saltValue;
		}
		
		public void setSaltValue(String saltValue) {
			this.saltValue = saltValue;
		}
		
		public String getHashedPassword() {
			return hashedPassword;
		}
		
		public void setHashedPassword(String hashedPassword) {
			this.hashedPassword = hashedPassword;
		}
    }


	
}

