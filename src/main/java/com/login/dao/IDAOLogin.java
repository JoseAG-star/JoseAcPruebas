package com.login.dao;

import java.util.List;

import com.login.modelo.Usuario;

public interface IDAOLogin {
   Usuario findUserByEmail (String email);

    boolean deleteById(int id);

    Usuario updateUser(Usuario userOld);

    Usuario findById(int id);

    int save(Usuario user);
    
    List<Usuario> findAll();

    Usuario findByUserName(String name);
}
