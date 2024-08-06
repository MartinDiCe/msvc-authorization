package com.diceprojects.msvclogin.persistences.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Entidad que representa un rol en el sistema.
 */
@Document(collection = "roles")
@Data
public class Role {

    @Id
    private String id;

    @Indexed(unique = true)
    private String role;

    private String description;
    private boolean deleted = false;
    private Date deleteDate;
    private Date createDate;
    private Date updateDate;
}
