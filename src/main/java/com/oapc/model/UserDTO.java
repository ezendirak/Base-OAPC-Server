package com.oapc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class UserDTO implements UserDetails {

	private static final long serialVersionUID = -5015027675142974514L;

	
    private Long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private boolean enabled;

    private Timestamp lastPasswordResetDate;

    private Set<Authority> authorities;

    
//    @JsonManagedReference
//	@OneToMany(mappedBy = "user")
//	private Set<Register> register = new HashSet();
   
//    @JsonBackReference
//    @ManyToOne
//	@JoinColumn(name="empresa", nullable = false)
    private String empresa;
    
//    @JsonIgnore
//	public Set<Register> getRegister() {
//		return register;
//	}
//
//	public void setRegister(Set<Register> register) {
//		this.register = register;
//	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.setLastPasswordResetDate( now );
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    
//    public void setEmpreses(Set<Empressa> empreses) {
//        this.empreses = empreses;
//    }
//    
//    public Set<Empressa> getEmprses(){
//    	return this.empreses;
//    }

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

   

}
