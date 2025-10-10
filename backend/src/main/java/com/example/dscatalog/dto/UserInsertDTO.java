package com.example.dscatalog.dto;

import com.example.dscatalog.services.validations.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Campo requerido")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
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
