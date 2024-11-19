package ca.mcgill.ecse321.PLMS.dto;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import io.swagger.v3.oas.annotations.media.Schema;

public class MonthlyCustomerResponseDto {
    @Schema(example= "customer@email.com", description = "Email linked to the account of the customer")
    private String email;
    @Schema(example= "Password1!", description = "Password linked to the account of the customer")
    private String password;
    @Schema(example= "customer", description = "Name of the customer")
    private String name;

    public MonthlyCustomerResponseDto(MonthlyCustomer monthlyCustomer) {
        this.email = monthlyCustomer.getEmail();
        this.password = monthlyCustomer.getPassword();
        this.name = monthlyCustomer.getName();
    }

    public MonthlyCustomerResponseDto() {}

    public String getEmail()
    { return email; }

    public String getPassword()
    { return password; }

    public String getName()
    { return name; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
