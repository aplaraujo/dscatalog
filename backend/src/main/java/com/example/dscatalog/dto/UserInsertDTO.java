package com.example.dscatalog.dto;

import com.example.dscatalog.services.validations.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private String password;

    public UserInsertDTO() {}

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public UserInsertDTO(Long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
