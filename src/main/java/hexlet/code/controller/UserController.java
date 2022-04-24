package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Operation(description = "Show user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is found"),
            @ApiResponse(responseCode = "404", description = "User with that id does not found")
    })
    @GetMapping("/users/{id}")
    public User getUserById(
            @Parameter(description = "User's ID")
            @PathVariable long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
    }

    @Operation(description = "Show list of users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(description = "Create new user")
    @ApiResponse(responseCode = "201", description = "User is created")
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(
            @Parameter(description = "User's data to save")
            @RequestBody UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @Operation(description = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated"),
            @ApiResponse(responseCode = "404", description = "User with that id does not found")
    })
    @PutMapping("/users/{id}")
    public User updateUser(
            @Parameter(description = "User's ID")
            @PathVariable long id,
            @Parameter(description = "User's data for update")
            @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(description = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is deleted"),
            @ApiResponse(responseCode = "404", description = "User with that id does not found")
    })
    @DeleteMapping("/users/{id}")
    public void deleteUser(
            @Parameter(description = "User's ID")
            @PathVariable long id) {
        userRepository.delete(userRepository.getById(id));
    }
}
