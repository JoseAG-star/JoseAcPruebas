package com.calidad.login.integracion;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import java.io.File;

import com.login.dao.IDAOLogin;
import com.login.dao.UserMysqlDAO;
import com.login.service.UserService;

public class LoginServiceTest extends DBTestCase {
private IDAOLogin idaoUser;
    private UserService servicio;
    public LoginServiceTest(){
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
         System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost");
    }

    @BeforeEach
    void setup() throws Exception{
        idaoUser= (IDAOLogin) new UserMysqlDAO();
        servicio = new UserService(idaoUser);

        IDatabaseConnection connection = getConnection();
        if (connection == null){
            fail("failed to establish a connection to the database.");
        }else{
            System.out.println("Connection established succesfully");
        }
        try{
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, gDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(connection, gDataSet());
        }catch(Exception e){
            fail("Error in setup: "+ e.getMessage());
        } finally{
            connection.close();
        }
    }

protected IDataSet gDataSet() throws Exception{
    return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB"));
}
@test 
public void WhenSaverUser_test(){
        servicio.createUser("usuario1", "usuario@mail.com", "12345");

        try{
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("usuarios");
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");
            Assertion.assertEquals(expectedTable, actualTable);

        }catch(Exception e){
            fail("Error in insert test:" + e.getMessage());
        }
    }

@Override
protected IDataSet getDataSet() throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDataSet'");
}

}
