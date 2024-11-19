package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

public class EmployeeResponseDto {

    @Schema(example= "employee@email.com", description = "Email linked to the account of the employee")
    private String email;
    @Schema(example= "Password1!", description = "Password linked to the account of the employee")
    private String password;
    @Schema(example= "employee", description = "Name of the employee")
    private String name;
    @Schema(example= "Mechanic", description = "Job title of the employee")
    private String jobTitle;
    @Schema(example= "25.25", description = "Hourly wage of the employee")
    private double hourlyWage;

    public EmployeeResponseDto(Employee employee) {
        this.email = employee.getEmail();
        this.password = employee.getPassword();
        this.name = employee.getName();
        this.jobTitle = employee.getJobTitle();
        this.hourlyWage = employee.getHourlyWage();
    }

    EmployeeResponseDto() {}

    public String getEmail()
    { return email; }

    public String getPassword()
    { return password; }

    public String getName()
    { return name; }

    public String getJobTitle()
    { return jobTitle; }

    public double getHourlyWage()
    { return hourlyWage; }
}
