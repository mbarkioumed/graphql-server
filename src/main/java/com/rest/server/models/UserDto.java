package com.rest.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userTitle;
    private String userFirstName;
    private String userLastName;
    private String userGender;
    private String userEmail;
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
