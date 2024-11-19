package ca.mcgill.ecse321.PLMS.dto;
import ca.mcgill.ecse321.PLMS.model.Service;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ServiceRequestDto {
    @NotBlank(message = "Service name cannot be blank.")
    @Schema(example= "Wheel Replacement", description = "Name of the service", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;
    @Min(value = 0, message = "Cost needs to be a number greater or equal to 0")
    @Schema(example= "50.25", description = "Cost of the service", requiredMode = Schema.RequiredMode.REQUIRED)
    private double cost;
    @Min(value = 0, message = "Length in hours needs to be a number greater or equal to 0.")
    @Schema(example= "2.0", description = "The estimated amount of time it will take to perform the service", requiredMode = Schema.RequiredMode.REQUIRED)
    private double lengthInHours;

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public double getLengthInHours() {
        return lengthInHours;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setLengthInHours(double lengthInHours) {
        this.lengthInHours = lengthInHours;
    }

    public Service toModel(){
        return new Service(serviceName, cost, lengthInHours);
    }
}

