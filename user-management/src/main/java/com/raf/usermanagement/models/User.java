package com.raf.usermanagement.models;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class User {

    @Id
    private String email;

    private String name;
    private String surname;
    private String password;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "can_create_user", column = @Column(name = "can_create_user")),
        @AttributeOverride(name = "can_read_user", column = @Column(name = "can_read_user")),
        @AttributeOverride(name = "can_update_user", column = @Column(name = "can_update_user")),
        @AttributeOverride(name = "can_delete_user", column = @Column(name = "can_delete_user"))
    })
    private Permission permission;

    public void setEmail(String email) {
        this.email = email;
    }

    @Id
    public String getEmail() {
        return email;
    }
    
}
