package ca.mcgill.ecse321.PLMS.dto;

import java.sql.Time;
import java.time.LocalDate;

import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 
 */
public class ServiceAppointmentRequestDto {

    //=-=-=-=-=- jakarta validation of variables -=-=-=-=-=//
    @NotNull(message = "Cannot have an empty date.")
    @FutureOrPresent(message = "Date must be in the future.")
    @Schema(example= "2024-05-05", description = "Date of the service appointment", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate date;
    
    @NotNull(message = "Cannot have an empty start time.")
    @Schema(example= "15:00:00", description = "The starting time of the service appointment", requiredMode = Schema.RequiredMode.REQUIRED, implementation = String.class, format = "HH:mm:ss")
    private Time startTime;

    @NotNull(message = "Cannot have an empty service name.")
    @Schema(example= "Wheel Replacement", description = "Name of the service that will be performed during the service appointment", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;

    @Schema(example= "user@email.com", description = "Email linked to the account of the customer (this is optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String userEmail;
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//

    /**
     * Method to construct a serviceAppointment object from a serviceAppointment request dto object
     * 
     * @return serviceAppointment object with all corresponding attributes
     */
	public ServiceAppointment toModel(Service service, MonthlyCustomer monthlyCustomer) {
		ServiceAppointment s = new ServiceAppointment();
		s.setDate(date);
		s.setStartTime(startTime);
		s.setService(service);
        s.setCustomer(monthlyCustomer);
		
		return s;
	}

    //=-=-=-=-=- Setters -=-=-=-=-=//
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setStartTime(Time starTime) {
        this.startTime = starTime;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//

    //=-=-=-=-=- Getters -=-=-=-=-=//
    public LocalDate getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }
	
    public String getServiceName() {
        return serviceName;
    }

    public String getUserEmail() {
        return userEmail;
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
}
