package com.calidad.login.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.InputStream;

import org.junit.Test;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import com.login.dao.UserMysqlDAO;
import com.login.modelo.Usuario;
import com.login.service.UserService;
import org.junit.Before; 


public class UserServiceTest extends DBTestCase {
private final UserMysqlDAO dao;
    private final UserService service;

    public UserServiceTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456"); 
    dao = new UserMysqlDAO();
    service = new UserService(dao);
    }
@Before
    @Override
    public void setUp() throws Exception {
        super.setUp(); 
        IDatabaseConnection connection = getConnection();
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("initDB.xml");
        return new FlatXmlDataSetBuilder().build(is);
    }
    
    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }
// Test: testCrearUsuario_ComparandoXML
// Este test inserta un nuevo usuario mediante el servicio y compara el estado final de la tabla con un conteo esperado.
// Se espera que la tabla usuarios contenga 4 registros (3 del dataset inicial + 1 creado).
    @Test
    public void testCrearUsuario_ComparandoXML() throws Exception {
        service.createUser("userNew", "nuevo@email.com", "Password123");

        IDatabaseConnection conn = getConnection();
        int iniciales = conn.createDataSet().getTable("usuarios").getRowCount();
    System.out.println(">>> USUARIOS INICIALES CARGADOS POR DBUNIT: " + iniciales);
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        assertEquals(4, actualTable.getRowCount());
    }
// Test: testFindUserByEmail
// Este test busca un usuario existente en el dataset cargado (initDB.xml) por su correo.
// Se espera que el usuario no sea nulo y que su nombre sea "user1".

    @Test
    public void testFindUserByEmail() {
        Usuario u = service.findUserByEmail("user1@email.com");
        assertNotNull(u);
        assertEquals("user1", u.getName());
    }
// Test: testDeleteUser
// Este test elimina un usuario existente (ID 1) de la base de datos cargada.
// Se espera que el conteo de filas en la tabla 'usuarios' baje a 2.
    
    @Test
    public void testDeleteUser() throws Exception {
        service.deleteUser(1);
        
        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        
        assertEquals(2, actualTable.getRowCount());
    }
    // Test: testUpdateUser
// Este test actualiza el nombre y contraseña del usuario con ID 2.
// Se espera que al consultar la base de datos, el nombre sea "NombreCambiado" y la contraseña "NuevaPass123".
    @Test
    public void testUpdateUser() throws Exception {
        Usuario usuario = service.findUserById(2);
        usuario.setName("NombreCambiado");
        usuario.setPassword("NuevaPass123");
        
        service.updateUser(usuario);

        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
     
        String nombreEnBD = (String) actualTable.getValue(1, "name");
        String passEnBD = (String) actualTable.getValue(1, "password");
        
        assertEquals("NombreCambiado", nombreEnBD);
        assertEquals("NuevaPass123", passEnBD);
    }
// Test: testFindAllUsers
// Este test consulta todos los usuarios cargados inicialmente por DBUnit.
// Se espera que el total de usuarios recuperados sea 3.
    @Test
    public void testFindAllUsers() throws Exception {
        int total = service.findAllUsers().size();
        assertEquals(3, total);
    }

}
