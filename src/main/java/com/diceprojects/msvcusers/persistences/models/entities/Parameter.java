package com.diceprojects.msvcusers.persistences.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un parámetro de configuración en el sistema.
 */
@Document(collection = "parameters")
@Data
public class Parameter {

    @Id
    private String id;

    @Indexed(unique = true)
    private String parameterName;

    private String value;
    private String description;
}
