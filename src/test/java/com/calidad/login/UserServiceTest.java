package com.calidad.login;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.login.dao.IDAOLogin;
import com.login.modelo.Usuario;
import com.login.service.UserService;

public class UserServiceTest {

 private IDAOLogin dao;
    private UserService service;

    @BeforeEach
    void setUp() {
        dao = mock(IDAOLogin.class);
        service = new UserService(dao);
    }

    @Test
    void testCreateUser_HappyPath() {
        String name = "Jose";
        String email = "jose@correo.com";
        String password = "Hola1234"; 
        
        when(dao.findUserByEmail(email)).thenReturn(null);
        when(dao.save(any(Usuario.class))).thenReturn(1);

        Usuario resultado = service.createUser(name, email, password);

        assertThat(resultado, is(notNullValue()));
        assertThat(resultado.getName(), is(name));
        assertThat(resultado.getId(), is(1));
    }

    @Test
    void testCreateUser_ShortPassword() {
        String name = "Jose";
        String email = "jose@correo.com";
        String shPassword = "1234"; 
        
        Usuario resultado = service.createUser(name, email, shPassword);

        assertThat(resultado, is(nullValue()));
        verify(dao, never()).save(any(Usuario.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        String name = "jose";
        String email = "yaexiste@correo.com";
        String password = "contrasena1234";
        Usuario usuarioExistente = new Usuario(name, email, password);
        
        when(dao.findUserByEmail(email)).thenReturn(usuarioExistente);

        service.createUser(name, email, password);
        verify(dao, never()).save(any(Usuario.class));
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        when(dao.deleteById(userId)).thenReturn(true);

        boolean resultado = service.deleteUser(userId);

        assertThat(resultado, is(true));
        verify(dao).deleteById(userId);
    }

    @Test
    void testUpdateUser() {
        Usuario usuarioCambios = new Usuario("NewName", "email@email.com", "NewPass123");
        usuarioCambios.setId(1);
        Usuario usuarioViejo = new Usuario("OldName", "email@email.com", "OldPass");
        usuarioViejo.setId(1);

        when(dao.findById(1)).thenReturn(usuarioViejo);
        when(dao.updateUser(any(Usuario.class))).thenReturn(usuarioCambios);

        Usuario resultado = service.updateUser(usuarioCambios);

        assertThat(resultado.getPassword(), is("NewPass123"));
    }

    @Test
    void testFindAllUsers() {
        List<Usuario> lista = Arrays.asList(
            new Usuario("u1", "e1", "p1"),
            new Usuario("u2", "e2", "p2")
        );
        when(dao.findAll()).thenReturn(lista);

        List<Usuario> resultado = service.findAllUsers();

        assertThat(resultado.size(), is(2));
    }
    
    @Test
    void testFindUserByEmail() {
        String email = "buscar@email.com";
        Usuario usuarioEsperado = new Usuario("Buscado", email, "Pass123");
        when(dao.findUserByEmail(email)).thenReturn(usuarioEsperado);

        Usuario resultado = service.findUserByEmail(email);

        assertThat(resultado, is(usuarioEsperado));
        verify(dao).findUserByEmail(email);
    }
   
        
}
