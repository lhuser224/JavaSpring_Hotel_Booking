package com.hotel.hotel_booking.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_passport_number")
    private String idPassportNumber;

    private String phone;

    private String username;

    private String password;

    private Boolean enabled;

    public User(){}

    public Integer getUserId(){ return userId; }
    public void setUserId(Integer userId){ this.userId = userId; }

    public String getFullName(){ return fullName; }
    public void setFullName(String fullName){ this.fullName = fullName; }

    public String getIdType(){ return idType; }
    public void setIdType(String idType){ this.idType = idType; }

    public String getIdPassportNumber(){ return idPassportNumber; }
    public void setIdPassportNumber(String idPassportNumber){ this.idPassportNumber = idPassportNumber; }

    public String getPhone(){ return phone; }
    public void setPhone(String phone){ this.phone = phone; }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    public Boolean getEnabled(){ return enabled; }
    public void setEnabled(Boolean enabled){ this.enabled = enabled; }
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "User_Roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    //Roles 
    private Set<Role> roles = new HashSet<>();
    public Set<Role> getRoles() { return roles;}
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}