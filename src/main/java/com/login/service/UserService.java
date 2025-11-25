package com.login.service;

import java.util.ArrayList;
import java.util.List;

import com.login.dao.IDAOLogin;
import com.login.modelo.Usuario;

public class UserService {
private IDAOLogin dao;
	
	public UserService(IDAOLogin dao) {
		this.dao = dao;
	}
	
	public Usuario createUser(String name, String email, String password) {
		Usuario user = null;
		if (password.length() >= 8 && password.length() <=16) {
			user = dao.findUserByEmail(email);
			
			if (user == null) {
				user = new Usuario(name, false, email,password);
				int id = dao.save(user);
				user.setId(id);
			}
		}
		return user;
	}
	
	public List<Usuario> findAllUsers(){
		List<Usuario> users = new ArrayList<Usuario>();
		users = dao.findAll();
	
		return users;
	}

	public Usuario findUserByEmail(String email) {
		
		return dao.findUserByEmail(email);
	}

public Usuario findUserById(int id) {
		
		return dao.findById(id);
	}
    
    public Usuario updateUser(Usuario user) {
    	Usuario userOld = dao.findById(user.getId());
    	userOld.setName(user.getName());
    	userOld.setPassword(user.getPassword());
    	return dao.updateUser(userOld);
    }

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }
}
