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
import org.junit.Before; // <--- IMPORTANTE
import org.junit.After;  // <--- IMPORTANTE

public class LoginServiceTest extends DBTestCase {
private UserMysqlDAO dao;
    private UserService service;

    public LoginServiceTest() {
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

    @Test
    public void testCrearUsuario_ComparandoXML() throws Exception {
        service.createUser("userNew", "nuevo@email.com", "Password123");

        IDatabaseConnection conn = getConnection();
        int iniciales = conn.createDataSet().getTable("usuarios").getRowCount();
    System.out.println(">>> USUARIOS INICIALES CARGADOS POR DBUNIT: " + iniciales);
        // Necesario desactivar aqui también si DBUnit se pone estricto
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios"); // Asegurate que coincida con tu tabla SQL
        
        // Debería haber 4 usuarios (3 del xml + 1 nuevo)
        assertEquals(4, actualTable.getRowCount());
    }

    @Test
    public void testFindUserByEmail() {
        Usuario u = service.findUserByEmail("user1@email.com");
        assertNotNull(u);
        assertEquals("user1", u.getName());
    }
    
    @Test
    public void testDeleteUser() throws Exception {
        service.deleteUser(1);
        
        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        
        assertEquals(2, actualTable.getRowCount());
    }
    @Test
    public void testUpdateUser() throws Exception {
        // 1. Obtener usuario existente (ID 2 según tu initDB.xml es user2)
        Usuario usuario = service.findUserById(2);
        usuario.setName("NombreCambiado");
        usuario.setPassword("NuevaPass123");
        
        // 2. Ejecutar actualización
        service.updateUser(usuario);
        
        // 3. Verificar en base de datos
        IDatabaseConnection conn = getConnection();
        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
        IDataSet databaseDataSet = conn.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");
        
        // Verificar que el nombre cambió en la fila correspondiente (fila 1 es el ID 2 porque empieza en 0)
        String nombreEnBD = (String) actualTable.getValue(1, "name");
        String passEnBD = (String) actualTable.getValue(1, "password");
        
        assertEquals("NombreCambiado", nombreEnBD);
        assertEquals("NuevaPass123", passEnBD);
    }

    @Test
    public void testFindAllUsers() throws Exception {
        // initDB.xml tiene 3 usuarios
        int total = service.findAllUsers().size();
        assertEquals(3, total);
    }

}
