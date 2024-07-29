package com.ivs.user_service.domain;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    @Id
    private String id;

    @JsonIgnore
    private boolean softDeleted = false;
    
    private Timestamp createdAt;
    @JsonIgnore
    private Timestamp updatedAt;

    private String mailAddress;
    private String firstName;
    private String middleName;
    private String lastName;

    @JsonIgnore
    private String saltValue;
    @JsonIgnore
    private String password;
    
}
