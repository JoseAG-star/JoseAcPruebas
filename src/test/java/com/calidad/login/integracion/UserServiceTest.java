package com.calidad.login.integracion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import java.util.List;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.login.dao.UserMysqlDAO;
import com.login.modelo.Usuario;
import com.login.service.UserService;

/**
 * Clase de Pruebas de Integración para UserService.
 * Se conecta a una base de datos MySQL en Docker para verificar
 */
public class UserServiceTest {
    // Gestor principal de DBUnit que maneja la conexión
    private IDatabaseTester databaseTester;

    private UserService service;
    
    // Constantes de conexión a la BD
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/calidad";
    private static final String USER = "root";
    private static final String PASS = "123456";

    /**
     * Configuración inicial que se ejecuta antes de cada @Test.
     * Establece la conexión con la base de datos.
     * Carga el dataset inicial (initDB.xml).
     * Ejecuta CLEAN_INSERT para borrar los datos existentes e insertar los del XML.
     * Esto garantiza que cada test inicie con la BD limpia.
     * Inicializa el servicio a probar.
     */

    @Before
    public void setUp() throws Exception {
        // 1. Configuración de conexión con el driver y credenciales
        databaseTester = new JdbcDatabaseTester(DRIVER, URL, USER, PASS);
        
        // 2. Definición del dataset que usaremos como estado inicial
        IDataSet dataSet = getDataSet();
        databaseTester.setDataSet(dataSet);
        
        // 3. Operación CLEAN_INSERT
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.onSetup();
        
        // 4. Inyección de dependencia del DAO al servicio
        service = new UserService(new UserMysqlDAO());
    }

    /**
     * Limpieza que se ejecuta después de cada @Test.
     * Cierra las conexiones abiertas por DBUnit.
     */
    @After
    public void tearDown() throws Exception {
        databaseTester.onTearDown();
    }

    /**
     * Método auxiliar para cargar el archivo XML con los datos de prueba.
     * Lee el archivo 'initDB.xml' de la carpeta src/test/resources.
     * Devuelve el IDataSet con la estructura de la tabla y datos.
     * Deuvelve una excepción si no encuentra el archivo o está mal formado.
     */
    protected IDataSet getDataSet() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("initDB.xml");
        return new FlatXmlDataSetBuilder().build(is);
    }
    
    /**
     * Obtiene una conexión activa para realizar verificaciones manuales dentro de los tests.
     * Configura la propiedad FEATURE_CASE_SENSITIVE_TABLE_NAMES en false para evitar
     * errores en sistemas operativos como Windows.
     */
    private IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection conn = databaseTester.getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        return conn;
    }
    
    // -------------------------------------------------------------------
    // PRUEBAS UNITARIAS
    // -------------------------------------------------------------------

    /**
     * Test: Crear Usuario
     * Objetivo: Verificar que se puede insertar un nuevo registro.
     * Validación: Se cuenta el número de filas. Debería aumentar de 3 a 4.
     */
    @Test
    public void testCrearUsuario_ComparandoXML() throws Exception {
        // 1. Ejecución
        service.createUser("userNew", "nuevo@email.com", "Password123");

        // 2. Verificación
        IDatabaseConnection conn = getConnection();
        int iniciales = conn.createDataSet().getTable("usuarios").getRowCount();
        System.out.println(">>> USUARIOS ACTUALES EN BD: " + iniciales);
        
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        
        // Esperamos 3 iniciales + 1 nuevo = 4
        assertEquals(5, actualTable.getRowCount());
    }

    /**
     * Test: Buscar Usuario por Email
     * Objetivo: Verificar que el servicio recupera los datos correctos.
     * Validación: Compara el objeto Java recuperado contra los datos exactos
     * definidos en el archivo 'initDB.xml'.
     */

    @Test
    public void testFindUserByEmail() throws Exception {
        // 1.Buscamos un usuario que sabemos que existe en el XML 
        String emailBusqueda = "user1@email.com";
        Usuario resultadoJava = service.findUserByEmail(emailBusqueda);
        
        assertNotNull("El usuario debería existir en la BD", resultadoJava);

        // 2.Cargamos el XML para verificar
        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable("usuarios");
        
        // Extraemos los valores esperados de la primera fila del XML
        String nombreEsperado = (String) expectedTable.getValue(0, "name");
        String emailEsperado = (String) expectedTable.getValue(0, "email");
        String passwordEsperado = (String) expectedTable.getValue(0, "password");

        // Validamos la integridad de los datos
        assertEquals(nombreEsperado, resultadoJava.getName());
        assertEquals(emailEsperado, resultadoJava.getEmail());
        assertEquals(passwordEsperado, resultadoJava.getPassword());
    }

    /**
     * Test: Borrar Usuario
     * Objetivo: Verificar que se elimina un registro por ID.
     * Validación: El conteo de filas debe disminuir de 3 a 2.
     */
    @Test
    public void testDeleteUser() throws Exception {
        service.deleteUser(1);
        
        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        
        assertEquals(2, actualTable.getRowCount());
    }

    /**
     * Test: Actualizar Usuario
     * Objetivo: Verificar la modificación de datod
     * Validación: Se consultan directamente los campos en la BD para asegurar
     * que el cambio persistió.
     */
    @Test
    public void testUpdateUser() throws Exception {
        // 1. Preparación
        Usuario usuario = service.findUserById(2);
        usuario.setName("NombreCambiado");
        usuario.setPassword("NuevaPass123");
        
        // 2. Ejecución
        service.updateUser(usuario);

        // 3. Verificación directa en base de datos
        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");

        // Buscamos en la fila 1 (ID 2 en el XML ordenado)
        String nombreEnBD = (String) actualTable.getValue(1, "name");
        String passEnBD = (String) actualTable.getValue(1, "password");
        
        assertEquals("NombreCambiado", nombreEnBD);
        assertEquals("NuevaPass123", passEnBD);
    }
    
    /**
     * Test: Buscar Todos los Usuarios
     * Objetivo: Verificar que se recupera la lista completa.
     * Validación: Se compara el tamaño de la lista y el contenido del primer elemento
     * contra el dataset original.
     */
    @Test
    public void testFindAllUsers() throws Exception {
        List<Usuario> lista = service.findAllUsers();

        // Cargamos el XML de referencia
        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable("usuarios");

        // 1. Verificamos cantidad
        assertEquals(expectedTable.getRowCount(), lista.size());

        // 2. Verificamos contenido
        String nombreXML = (String) expectedTable.getValue(0, "name");
        String nombreJava = lista.get(0).getName();
        
        assertEquals("El nombre del primer usuario no coincide con el XML", nombreXML, nombreJava);
    }
}