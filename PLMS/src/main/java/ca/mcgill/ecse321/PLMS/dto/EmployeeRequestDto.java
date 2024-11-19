package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


public class EmployeeRequestDto {

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must follow this format xxx@email.address")
    @Schema(example= "employee@email.com", description = "Email linked to the account of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^+=]).+$", message = "Password contains at least one uppercase, lowercase and special character [!@#$%^+=]")
    @Size(min = 5, max = 13, message = "Password must have 5-13 character" )
    @NotBlank(message = "Password cannot be blank.")
    @Schema(example= "Password1!", description = "Password linked to the account of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "Name can only have letters")
    @NotBlank(message = "Name cannot be blank.")
    @Schema(example= "employee", description = "Name of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Job title must not be blank")
    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "Job title can only have letters")
    @Schema(example= "Mechanic", description = "Job title of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jobTitle;

    @Schema(example= "25.25", description = "Hourly wage of the employee", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double hourlyWage;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public String getPassword() {
        return password;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setEmail(String aEmail)
    { this.email = aEmail; }

    public void setPassword(String aPassword)
    { this.password = aPassword; }

    public void setName(String aName)
    { this.name = aName; }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Employee toModel() {
        return new Employee(email, password, name, jobTitle, hourlyWage);
    }
}
