package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    @Size(min = 2, max = 10, message = "Invalid Firstname (2-10 Characters) ")
    private String firstName;

    @Size(min = 2, max = 10, message = "Invalid Lastname (2-10 Characters) ")
    private String lastName;

    private String username;

    @Size(min = 6, max = 20, message = "Invalid Password (6-20 Characters) ")
    private String password;

    private String repeatPassword;


}
