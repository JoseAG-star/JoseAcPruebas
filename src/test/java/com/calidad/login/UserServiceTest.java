package com.calidad.login;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.login.dao.IDAOLogin;
import com.login.modelo.Usuario;
import com.login.service.UserService;

public class UserServiceTest {
private IDAOLogin idaoUser;
    private Usuario usuario;
    private UserService servicio;

    // @BeforeEach

    // @Test
    @Test
    void createUsuarioExitoso(){
        // SETUP
        String email = "correo@correo.com";
        String pass = "Password1234";
        String nombre = "Jose";
        int id = 1;

        // Definición del Mock del metodo findUsuarioByEmail
        usuario = new Usuario(email, false, nombre, pass);

        // Crear el Mock del método
        idaoUser = mock(IDAOLogin.class);

        // Definir el Mock del método
        when(idaoUser.findUserByEmail(email)).thenReturn(null);
    
        // Definir el mock del Método
        when(idaoUser.save(usuario)).thenReturn(1);
    
        // Instanciar la clase que voy a probar
        servicio = new UserService(idaoUser);

        // Ejercicio
        Usuario resultadoEjecucion = servicio.createUser(email, nombre, pass);
        resultadoEjecucion.setId(id);

        // Verificacion
        assertThat(resultadoEjecucion.getId(),is(id));
        assertThat(resultadoEjecucion.getEmail(),is(email));
        assertThat(resultadoEjecucion.getPassword(),is(pass));
        assertThat(resultadoEjecucion.getName(),is(nombre));

    }

    @Test
    void createUsuarioExitosoBD(){
        // SETUP
        String email = "correo@correo.com";
        String pass = "Password1234";
        String nombre = "Jose";
        int id = 1;
        HashMap <Integer, Usuario> db = new HashMap<Integer, Usuario>();
 
        // Definición del Mock del metodo findUsuarioByEmail
        usuario = new Usuario(email, false, nombre, pass);

        // Crear el Mock del método
        idaoUser = mock(IDAOLogin.class);

        // Definir el Mock del método
        when(idaoUser.findUserByEmail(email)).thenReturn(null);
    
        // Definir el mock del Método
        when(idaoUser.save(any(Usuario.class))).thenAnswer(new Answer<Integer>() {
			
			//Method within the class
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				//set behavior in every invocation
				Usuario arg = (Usuario) invocation.getArguments()[0];
				db.put(db.size()+1, arg);
				System.out.println("Size after = " + db.size() + "\n");
				
				//Return the invoked value
				return db.size();
				}
			}
		);
    
        int tantes = db.size();
        // Instanciar la clase que voy a probar
        servicio = new UserService(idaoUser);

        // Ejercicio
        Usuario resultadoEjecucion = servicio.createUser(email, nombre, pass);

        // Verificacion
        assertThat(tantes+1, is(db.size()));

    }
}
