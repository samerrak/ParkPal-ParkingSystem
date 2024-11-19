package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AccountRequestDto //no longer needed
{

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must follow this format xxx@email.address")
    @Schema(example= "employee@email.com", description = "Email linked to the account of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&+=]).+$", message = "Password contains at least one uppercase, lowercase and special character [!@#$%^&+=]")
    @Size(min = 5, max = 13, message = "Password must have 5-13 character" )
    @NotBlank(message = "Password cannot be blank.")
    @Schema(example= "Password1!", description = "Password linked to the account of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Pattern(regexp = "(Employee|Customer|Owner)", message = "User must either employee, customer, owner")
    @NotBlank(message = "Name cannot be blank.")
    private String user;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
