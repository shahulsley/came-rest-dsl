package dev.shahul.came_rest_dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDTO {

    @JsonIgnore
    @Id
    private String documentId;

    private String shipperNumber; 
    private String startDate;
    private String endDate;
    private String returnCode;
    private String message;

    public RequestDTO(String shipperNumber) {
        this.shipperNumber = shipperNumber;
    }

}
