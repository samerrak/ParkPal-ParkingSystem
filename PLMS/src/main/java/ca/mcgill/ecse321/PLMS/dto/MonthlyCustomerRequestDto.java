package ca.mcgill.ecse321.PLMS.dto;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class MonthlyCustomerRequestDto {

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must follow this format xxx@email.address")
    @Schema(example= "user@email.com", description = "Email linked to the account of the customer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^+=]).+$", message = "Password contains at least one uppercase, lowercase and special character [!@#$%^+=]")
    @Size(min = 5, max = 13, message = "Password must have 5-13 character" )
    @NotBlank(message = "Password cannot be blank.")
    @Schema(example= "Password1!", description = "Password linked to the account of the customer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "Name can only have letters")
    @NotBlank(message = "Name cannot be blank.")
    @Schema(example= "customer", description = "Name of the customer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    public void setEmail(String aEmail)
    { this.email = aEmail; }

    public void setPassword(String aPassword)
    { this.password = aPassword; }

    public void setName(String aName)
    { this.name = aName; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public MonthlyCustomer toModel() {
        return new MonthlyCustomer(email, password, name);
    }

}
