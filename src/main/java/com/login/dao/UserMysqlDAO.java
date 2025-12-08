package com.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.login.modelo.Usuario;

public class UserMysqlDAO implements IDAOLogin {

public Connection getConnectionMySQL() {

		Connection con = null;
		try {
			// Establish the driver connector
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Set the URI for connecting the MySql database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/calidad", "root", "123456");
		} catch (Exception e) {
			System.out.println("Error al obtener conexión: " + e);
		}
		return con;
	}

	@Override
	public Usuario findByUserName(String name) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		
		Usuario result = null;

		try {
			// Declare statement query to run
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE name = ?");
			// Set the values to match in the ? on query
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();

			// Obtain the pointer to the data in generated table
			rs.next();

			int id = rs.getInt(1);
			String username  = rs.getString(2);
			String email = rs.getString(3);
			String password = rs.getString(4);
			boolean isLogged = rs.getBoolean(5);

			result = new Usuario(username, password, email);
			result.setId(id);
			result.setIsLogged(isLogged);

			// Return the values of the search
			System.out.println("\n");
			System.out.println("---Alumno---");
			System.out.println("ID: " + result.getId());
			System.out.println("Nombre: " + result.getName());
			System.out.println("Email: " + result.getEmail());
			System.out.println("Tipo: " + result.isIsLogged() + "\n");
			// Close connection with the database
			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
					
	}

	@Override
	public int save(Usuario user) {
		Connection connection = getConnectionMySQL();
		int result = -1;
		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("insert INTO usuarios(name,email,password,isLogged) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			// Set the values to match in the ? on query
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setBoolean(4, user.isIsLogged());

			// Return the result of connection nad statement
			if (preparedStatement.executeUpdate() >= 1) {
				try(ResultSet rs = preparedStatement.getGeneratedKeys()){
					if (rs.next()) {
						result = rs.getInt(1);
					}
				}
			}
			System.out.println("\n");
			System.out.println("Alumno añadido con exito");
			System.out.println(">> Return: " + result + "\n");
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

	
	public Usuario findUserByEmail(String email) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		
		Usuario result = null;

		try {
			// Declare statement query to run
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE email = ?");
			// Set the values to match in the ? on query
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();

			// Obtain the pointer to the data in generated table
			//rs.next();

			if (rs.next()) {
				int id = rs.getInt(1);
				String username  = rs.getString(2);
				String emailUser = rs.getString(3);
				String password = rs.getString(4);
				boolean isLogged = rs.getBoolean(5);
	
				result = new Usuario(username, emailUser, password);
				result.setId(id);
				result.setIsLogged(isLogged);
	
				// Return the values of the search
				System.out.println("\n");
				System.out.println("---Alumno---");
				System.out.println("ID: " + result.getId());
				System.out.println("Nombre: " + result.getName());
				System.out.println("Email: " + result.getEmail());
				System.out.println("Tipo: " + result.isIsLogged() + "\n");
			}	else {
				System.out.println("No user found with email");
			}
			
			
			// Close connection with the database
			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
					
	}

	@Override
	public List<Usuario> findAll() {
		Connection connection = getConnectionMySQL();
		  PreparedStatement preparedStatement;
		  ResultSet rs;
		  Usuario retrieved = null;

		  List<Usuario> listaAlumnos = new ArrayList<Usuario>();
		  
		  try {
		   // Declare statement query to run
		   preparedStatement = connection.prepareStatement("SELECT * from usuarios");
		   // Set the values to match in the ? on query
		   rs = preparedStatement.executeQuery();

		   // Obtain the pointer to the data in generated table
		   while (rs.next()) {

			   int id = rs.getInt(1);
			   String name = rs.getString(2);
			   String email = rs.getString(3);
			   String password = rs.getString(4);
			   boolean log = rs.getBoolean(5);		 
			   retrieved = new Usuario(name, email,password);
			   retrieved.setId(id);
			   retrieved.setIsLogged(log);
			   listaAlumnos.add(retrieved);
		   }
		   
			   connection.close();
			   rs.close();
			   preparedStatement.close();
	
			  } catch (Exception e) {
			   System.out.println(e);
			  }
			  return listaAlumnos;
		  
		}

	

	@Override
	public Usuario findById(int id) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		
		Usuario result = null;

		try {
			// Declare statement query to run
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			// Obtain the pointer to the data in generated table
			rs.next();
			String username  = rs.getString(2);
			String email = rs.getString(3);
			String password = rs.getString(4);
			boolean isLogged = rs.getBoolean(5);

			result = new Usuario(username, email, password);
			result.setId(id);
			result.setIsLogged(isLogged);

			// Return the values of the search
			System.out.println("\n");
			System.out.println("---Alumno---");
			System.out.println("ID: " + result.getId());
			System.out.println("Nombre: " + result.getName());
			System.out.println("Email: " + result.getEmail());
			System.out.println("Tipo: " + result.isIsLogged() + "\n");
			// Close connection with the database
			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
					
	}

	@Override
	public boolean deleteById(int id) {
		Connection connection = getConnectionMySQL();
		boolean result = false;

		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("Delete from usuarios WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setInt(1, id);

			// Return the result of connection and statement
			if (preparedStatement.executeUpdate() >= 1) {
				result = true;
			}
			System.out.println("\n");
			System.out.println("Alumno eliminado con exito");
			System.out.println(">> Return: " + result + "\n");
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	
	}

	@Override
	public Usuario updateUser(Usuario userNew) {
		Connection connection = getConnectionMySQL();
		Usuario result = null;

		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("UPDATE usuarios SET name = ?,password= ? WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setString(1, userNew.getName());
			preparedStatement.setString(2, userNew.getPassword());
			preparedStatement.setInt(3, userNew.getId());
			// Return the result of connection and statement
			if (preparedStatement.executeUpdate() >= 1) {
				result = userNew;
			}
			System.out.println("\n");
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	
	}
}

