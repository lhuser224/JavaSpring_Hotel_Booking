package com.hotel.hotel_booking.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    public Role(){}

    public Integer getRoleId(){ return roleId; }
    public void setRoleId(Integer roleId){ this.roleId = roleId; }

    public String getRoleName(){ return roleName; }
    public void setRoleName(String roleName){ this.roleName = roleName; }
    
    public Set<User> getUsers() { return users; }

    public void setUsers(Set<User> users) { this.users = users; }
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}