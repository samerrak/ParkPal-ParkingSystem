package ca.mcgill.ecse321.PLMS.dto;

import java.sql.Time;
import java.time.LocalDate;

import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import io.swagger.v3.oas.annotations.media.Schema;

public class ServiceAppointmentResponseDto {
   
    @Schema(example= "5", description = "id of the service appointment")
    private int id;
    @Schema(example= "2024-05-05", description = "Date of the service appointment")
    private LocalDate date;
    @Schema(example= "15:00:00", description = "The starting time of the service appointment", implementation = String.class, format = "HH:mm:ss")
    private Time startTime;
    @Schema(example= "17:00:00", description = "The predicted end time of the service appointment", implementation = String.class, format = "HH:mm:ss")
    private Time endTime;

    //Relationships
    @Schema(example= "user@email.com", description = "Email linked to the account of the customer (this is optional)")
    private String customerEmail;
    @Schema(example= "employee@email.com", description = "Email linked to the account of the employee that will perform the service (if empty, no employee is available at this time)")
    private String employeeEmail;
    @Schema(example= "Wheel Replacement", description = "Name of the service that will be performed during the service appointment")
    private String serviceName;

    public ServiceAppointmentResponseDto() {}

    public ServiceAppointmentResponseDto(ServiceAppointment s){
        this.id = s.getId();
        this.date = s.getDate();
        this.startTime = s.getStartTime();
        this.endTime = s.getEndTime();

        if (s.getCustomer() != null) this.customerEmail = s.getCustomer().getEmail();
        if (s.getEmployee() != null) this.employeeEmail = s.getEmployee().getEmail();
        this.serviceName = s.getService().getServiceName();
    }

    //=-=-=-=-=- Getters -=-=-=-=-=//
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getServiceName() {
        return serviceName;
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//

    //=-=-=-=-=- Setters -=-=-=-=-=//
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
}
