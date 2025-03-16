package com.rest.server.graphql;

import com.rest.server.models.Location;
import com.rest.server.models.User;
import com.rest.server.models.UserDto;
import com.rest.server.services.LocationService;
import com.rest.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserResolver {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    // Convert User to UserDto
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUserTitle(user.getUserTitle());
        userDto.setUserFirstName(user.getUserFirstName());
        userDto.setUserLastName(user.getUserLastName());
        userDto.setUserGender(user.getUserGender());
        userDto.setUserEmail(user.getUserEmail());
        userDto.setUserDateOfBirth(user.getUserDateOfBirth());
        userDto.setUserRegisterDate(user.getUserRegisterDate());
        userDto.setUserPhone(user.getUserPhone());
        userDto.setUserPicture(user.getUserPicture());
        userDto.setUserLocationId(user.getUserLocationId());
        return userDto;
    }

    // Query to get all users with pagination
    @QueryMapping
    public List<UserDto> users(@Argument Integer page, @Argument Integer limit) {
        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<User> usersPage = userService.allUsers(PageRequest.of(pageNum, pageSize));
        return usersPage.map(this::convertToDto).getContent();
    }

    // Query to get a single user by ID
    @QueryMapping
    public User user(@Argument String id) {
        Optional<User> userOptional = userService.singleUser(id);
        return userOptional.orElse(null);
    }

    // Mutation to create a user
    @MutationMapping
    public User createUser(@Argument String firstName, @Argument String lastName,
            @Argument String email, @Argument String password) {

        User newUser = new User();
        newUser.setUserFirstName(firstName);
        newUser.setUserLastName(lastName);
        newUser.setUserEmail(email);
        newUser.setUserPassword(password); // Use the provided password

        return userService.createUser(newUser);
    }

    // Mutation to update a user
    @MutationMapping
    public User updateUser(@Argument String id, @Argument String title, @Argument String firstName,
            @Argument String lastName) {

        Optional<User> userOptional = userService.singleUser(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (title != null)
                user.setUserTitle(title);
            if (firstName != null)
                user.setUserFirstName(firstName);
            if (lastName != null)
                user.setUserLastName(lastName);

            return userService.updateUser(id, user);
        }
        return null;
    }

    // Mutation to delete a user
    @MutationMapping
    public String deleteUser(@Argument String id) {
        userService.deleteUser(id);
        return id;
    }

    // Resolver for location field in User type
    @SchemaMapping(typeName = "User", field = "location")
    public Location location(User user) {
        if (user.getUserLocationId() != null) {
            return locationService.singleLocation(user.getUserLocationId()).orElse(null);
        }
        return null;
    }
}