package com.login.service;

import com.login.dao.IDAOLogin;
import com.login.modelo.Usuario;

public class LoginService {
IDAOLogin dao;

public LoginService(IDAOLogin d){
    dao = d;
}
 public boolean login(String email, String password){
        Usuario u = dao.findUserByEmail(email);
        if(u != null){
            if(u.getPassword() == password){
                u.setIsLogged(true);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
}
}
