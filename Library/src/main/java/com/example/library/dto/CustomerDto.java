package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 2, max = 10, message = "Invalid Firstname (2-10 Characters) ")
    private String firstName;

    @Size(min = 2, max = 15, message = "Invalid Lastname (2-10 Characters) ")
    private String lastName;
    @Column(unique = true)
    private String username;

    private String address;
    @Size(min = 10, max = 13, message = "Invalid Phone Number it's 10 or 13 Numbers")
    private String phone;

    @Size(min = 6, max = 20, message = "Invalid Password (6-20 Characters) ")
    private String password;

    private String repeatPassword;

}
