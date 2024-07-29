package com.ivs.order_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String mailAddress;
    private String firstName;
    private String middleName;
    private String lastName;
}