package com.rest.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String userId;

    private String userTitle;

    @NotBlank(message = "First name is mandatory")
    @NotNull
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String userFirstName;

    @NotBlank(message = "Last name is mandatory")
    @NotNull
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String userLastName;

    private String userGender;

    @Email(message = "Email should be valid")
    private String userEmail;

    @NotBlank(message = "Password is mandatory")
    private String userPassword;

    private String userDateOfBirth;
    private String userRegisterDate;
    private String userPhone;
    private String userPicture;
    private String userLocationId;

    public String getId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.userFirstName;
    }

    public String getLastName() {
        return this.userLastName;
    }

    public String getTitle() {
        return this.userTitle;
    }

    public String getGender() {
        return this.userGender;
    }

    public String getEmail() {
        return this.userEmail;
    }

    public String getDateOfBirth() {
        return this.userDateOfBirth;
    }

    public String getRegisterDate() {
        return this.userRegisterDate;
    }

    public String getPhone() {
        return this.userPhone;
    }

    public String getPicture() {
        return this.userPicture;
    }
}
