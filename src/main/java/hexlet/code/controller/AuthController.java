package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(description = "Login")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Correct data"),
            @ApiResponse(responseCode = "401", description = "Incorrect data")
    })
    @PostMapping("/login")
    public String login(
            @Parameter(description = "Data for login (email and password)")
            @RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto.getEmail(), loginDto.getPassword());
    }

}
