package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Service;
import io.swagger.v3.oas.annotations.media.Schema;

public class ServiceResponseDto {
    @Schema(example= "Wheel Replacement", description = "Name of the service")
    private String serviceName;
    @Schema(example= "50.25", description = "Cost of the service")
    private double cost;
    @Schema(example= "2.0", description = "The estimated amount of time it will take to perform the service")
    private double lengthInHours;

    public ServiceResponseDto(Service service) {
        this.serviceName = service.getServiceName();
        this.cost = service.getCost();
        this.lengthInHours = service.getLengthInHours();
    }

    public ServiceResponseDto(){}
    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public double getLengthInHours() {
        return lengthInHours;
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public void setLengthInHours(double lengthInHours){
        this.lengthInHours = lengthInHours;
    }
}
