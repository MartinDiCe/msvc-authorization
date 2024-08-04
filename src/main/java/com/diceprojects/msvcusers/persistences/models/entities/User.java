package com.diceprojects.msvcusers.persistences.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.Set;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private boolean active;
    private boolean deleted;
    private Date deleteDate;
    private Date createDate;
    private Date updateDate;
    private String securityToken;
    private boolean forcePasswordChange;

    @DBRef
    private Set<Role> roles;
}

