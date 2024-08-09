package com.diceprojects.msvclogin.mongo;

import com.diceprojects.msvclogin.persistences.models.entities.Role;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Listener para eventos de la entidad Role en MongoDB.
 * Se encarga de establecer las fechas de creación y actualización antes de convertir la entidad.
 */
@Component
public class RoleModelListener extends AbstractMongoEventListener<Role> {

    /**
     * Método que se ejecuta antes de convertir la entidad Role.
     * Establece la fecha de creación si no está presente.
     *
     * @param event el evento de antes de convertir de la entidad Role
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Role> event) {
        Role role = event.getSource();
        if (role.getCreateDate() == null) {
            role.setCreateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
        }
    }
}
