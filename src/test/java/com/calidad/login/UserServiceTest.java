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
      @SuppressWarnings("unused")
    void setUp() {
        dao = mock(IDAOLogin.class);
        service = new UserService(dao);
    }
// Test: testCreateUser_HappyPath
// Este test verifica el flujo ideal de creación de usuario, donde el email no existe y la contraseña es válida.
// Se espera que el resultado no sea nulo, el nombre sea "Jose" y el ID asignado sea 1
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
// Test: testCreateUser_ShortPassword
// Este test verifica la validación de seguridad cuando la contraseña es demasiado corta (menos de 8 caracteres).
// Se espera un resultado nulo (el usuario no se crea) y que el método save() del DAO nunca se ejecute.
    @Test
    void testCreateUser_ShortPassword() {
        String name = "Jose";
        String email = "jose@correo.com";
        String shPassword = "1234"; 
        
        Usuario resultado = service.createUser(name, email, shPassword);

        assertThat(resultado, is(nullValue()));
        verify(dao, never()).save(any(Usuario.class));
    }
// Test: testCreateUser_UserAlreadyExists
// Este test verifica que no se cree un usuario si el email ya existe en el sistema.
// Se espera que el método save() del DAO nunca sea llamado
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
// Test: testDeleteUser
// Este test verifica que el servicio llame correctamente al DAO para eliminar un usuario por ID.
// Se espera un resultado true indicando que la eliminación fue exitosa.
    @Test
    void testDeleteUser() {
        int userId = 1;
        when(dao.deleteById(userId)).thenReturn(true);

        boolean resultado = service.deleteUser(userId);

        assertThat(resultado, is(true));
        verify(dao).deleteById(userId);
    }
// Test: testUpdateUser
// Este test verifica la lógica de actualización, recuperando un usuario viejo y guardando los nuevos datos.
// Se espera que la contraseña del objeto retornado sea "NewPass123".
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
// Test: testFindAllUsers
// Este test verifica que el servicio pueda recuperar una lista completa de usuarios.
// Se espera que el tamaño de la lista retornada sea 2.
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
// Test: testFindUserByEmail
// Este test verifica la búsqueda de un usuario específico por su correo electrónico.
// Se espera que el objeto retornado sea igual al usuario simulado "usuarioEsperado".    
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
