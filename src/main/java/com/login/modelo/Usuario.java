package com.login.modelo;

public class Usuario {
private int id;
private String name;
private String email;
private String password;
private boolean IsLogged;

public Usuario(int id, String name, String email, String password, boolean isLogged) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    IsLogged = isLogged;
}
public Usuario(String email2, boolean b, String nombre, String pass) {
    //TODO Auto-generated constructor stub
}
public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}
public boolean isIsLogged() {
    return IsLogged;
}
public void setIsLogged(boolean isLogged) {
    IsLogged = isLogged;
}
}
