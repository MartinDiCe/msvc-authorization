package com.diceprojects.msvcauthorization.persistences.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad que representa un rol en el sistema.
 */
@Document(collection = "roles")
@Data
@Getter
@Setter
public class Role {

    @Id
    private String id;
    @Indexed(unique = true)
    private String role;
    private String description;
    private boolean deleted = false;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deleteDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateDate;

    public Role(String id, String role, String description, boolean deleted, String status, LocalDateTime deleteDate, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.role = role;
        this.description = description;
        this.deleted = deleted;
        this.status = status;
        this.deleteDate = deleteDate;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Role() {

    }
}
