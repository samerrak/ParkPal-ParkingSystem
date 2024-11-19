package ca.mcgill.ecse321.PLMS.dto;
import ca.mcgill.ecse321.PLMS.model.Owner;
import io.swagger.v3.oas.annotations.media.Schema;

public class OwnerResponseDto {
    @Schema(example= "owner@email.com", description = "Email linked to the account of the owner")
    private String email;
    @Schema(example= "Password1!", description = "Password linked to the account of the owner")
    private String password;
    @Schema(example= "owner", description = "Name of the owner")
    private String name;

    public OwnerResponseDto(Owner owner) {
        this.email = owner.getEmail();
        this.password = owner.getPassword();
        this.name = owner.getName();
    }

    public OwnerResponseDto() {}


    public String getEmail()
    { return email; }

    public String getPassword()
    { return password; }

    public String getName()
    { return name; }

}
