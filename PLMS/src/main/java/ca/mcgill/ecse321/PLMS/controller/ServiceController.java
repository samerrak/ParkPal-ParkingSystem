package ca.mcgill.ecse321.PLMS.controller;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import ca.mcgill.ecse321.PLMS.dto.ServiceRequestDto;
import ca.mcgill.ecse321.PLMS.dto.ServiceResponseDto;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.service.ServiceService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

/**
 * Controller class related to endpoints for CRUD operations on the service model class in the context of the PLMS system
 */
@CrossOrigin(origins="*")
@RestController
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    /**
     * Gets all services.
     *
     * @return All services.
     */
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "All services.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ServiceResponseDto.class)))}),
      @ApiResponse(responseCode = "404", description = "There are no services in the system.", content = {@Content(mediaType = "String")})
      })
    @GetMapping("/service")
    public Iterable<ServiceResponseDto> getAllServices(){
        return StreamSupport.stream(serviceService.getAllServices().spliterator(), false).map(s -> new ServiceResponseDto(s)).collect(Collectors.toList());
    }

    /**
     * Gets a service by the service name
     *
     * @param serviceName service name of the service to get
     * @return service with serviceName
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Service with this name does not exists.", content = {@Content(mediaType = "String")})
      })
    @GetMapping("/service/{serviceName}")
    public ResponseEntity<ServiceResponseDto> getServiceByServiceName(@PathVariable String serviceName){
        Service service = serviceService.getServiceByServiceName(serviceName);
        ServiceResponseDto responseBody = new ServiceResponseDto(service);
        return new ResponseEntity<ServiceResponseDto>(responseBody, HttpStatus.OK);
    }

    /**
     * Creates a service.
     *
     * @param serviceDto Contains service name (String), cost (int) and length in hours (double)
     * @return serviceDto of the created service
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Service with service this name already exists.", content = {@Content(mediaType = "String")})
      })
    @PostMapping("/service/create")
    public ResponseEntity<ServiceResponseDto> createService(@Valid @RequestBody ServiceRequestDto serviceDto){
        Service service = serviceDto.toModel();
        service = serviceService.createService(service);
        ServiceResponseDto responseBody = new ServiceResponseDto(service);
        return new ResponseEntity<ServiceResponseDto>(responseBody, HttpStatus.CREATED);
    }


    /**
     * Allows updates for all service variables
     *
     * @param serviceDto Contains service name (String), cost (int) and length in hours (double)
     * @return service with updated values
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Service with this name does not exists.", content = {@Content(mediaType = "String")})
      })
    @PutMapping("/service/")
    public ResponseEntity<ServiceResponseDto> updateServiceInfo(@RequestBody @Valid ServiceRequestDto serviceDto){
        Service service = serviceDto.toModel();
        service = serviceService.updateService(service);
        ServiceResponseDto responseBody = new ServiceResponseDto(service);
        return new ResponseEntity<ServiceResponseDto>(responseBody, HttpStatus.OK);
    }


    /**
     * Deletes a service
     * 
     * @param serviceName service name of the service to delete
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service succefully deleted."),
        @ApiResponse(responseCode = "404", description = "Service with this name does not exists.", content = {@Content(mediaType = "String")})
      })
    @DeleteMapping("/service/{serviceName}")
    public void deleteService(@PathVariable String serviceName){
        serviceService.deleteServiceByServiceName(serviceName);
    }

}


