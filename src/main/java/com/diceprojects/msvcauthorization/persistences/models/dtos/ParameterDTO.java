package com.diceprojects.msvcauthorization.persistences.models.dtos;

import lombok.Data;

/**
 * DTO para transferir los datos del parámetro entre microservicios.
 */
@Data
public class ParameterDTO {

    private String id;
    private String parameterName;
    private String value;
    private String description;

}
