/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.turc0160leve0282.Assignment2.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.Instance;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.HashMap;
/**
 *
 * @author windows
 */
@Entity
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "USERID")
    private String userId;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "GROUPNAME")
    private String groupName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return "";
    }

    public void setPassword(String password) {
          if (password == null || password.trim().isEmpty()) {
        return;
    }
        // initialize a PasswordHash object which will generate password hashes
        Instance<? extends PasswordHash> instance = CDI.current().select(Pbkdf2PasswordHash.class);
        PasswordHash passwordHash = instance.get();
        passwordHash.initialize(new HashMap<String,String>());  // todo: are the defaults good enough?
        // now we can generate a password entry for a given password
        String passwordEntry = password; 
        passwordEntry = passwordHash.generate(passwordEntry.toCharArray());
        //at this point, passwordEntry refers to a salted/hashed password entry String corresponding to the clear text “mySecretPassword”
       this.password = passwordEntry;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "cst8218.turc0160leve0282.Assignment2.entity.AppUser[ id=" + id + " ]";
    }
    
}
