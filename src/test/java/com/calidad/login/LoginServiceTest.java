package com.calidad.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.login.dao.IDAOLogin;
import com.login.modelo.Usuario;
import com.login.service.LoginService;

public class LoginServiceTest {
 private IDAOLogin idaoLogin;
    private Usuario usuario;
    private LoginService login;
   // Test: loginExitosoTest
// Este test verifica que el método login retorne verdadero cuando el usuario existe y la contraseña coincide.
// Se espera un resultado de true.
@Test void loginExitosoTest(){
    //Setup
String email = "Correo@correo.com";
String password = "contraseña123";
//Crear Mock de la dependencia
idaoLogin = mock(IDAOLogin.class);
usuario = new Usuario(password, email, password);
//Definir el mock del metodo FindUserByEmail
when (idaoLogin.findUserByEmail(email)).thenReturn(usuario);
//Instanciar la clase que probare
login = new LoginService(idaoLogin);
    //Ejercicio
boolean resultadoEjecucion = login.login(email, password);
    //Verificacion
boolean resultadoEsperado = true;

assertThat(resultadoEsperado,is(resultadoEjecucion));
    
}

}
