package customer.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customer.model.customer;



public class CustomerCntrl {

	 private String jdbcURL = "jdbc:mysql://localhost:3306/book_event";
	    private String jdbcUsername = "root";
	    private String jdbcPassword = "sashini@123";

	    private static final String INSERT_USERS_SQL = "INSERT INTO customer" + "  (name, email, eventname, eparticipant) VALUES " +
	        " (?, ?, ?,?);";

	    private static final String SELECT_USER_BY_ID = "select id,name,email,eventname,eparticipant from customer where id =?";
	    private static final String SELECT_ALL_USERS = "select * from customer";
	    private static final String DELETE_USERS_SQL = "delete from customer where id = ?;";
	    private static final String UPDATE_USERS_SQL = "update customer set name = ?,email= ?, country =? where id = ?;";

	    public CustomerCntrl() {}

	    protected Connection getConnection() {
	        Connection connection = null;
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return connection;
	    }

	    public void insertUser(customer customer) throws SQLException {
	        System.out.println(INSERT_USERS_SQL);
	        // try-with-resource statement will auto close the connection.
	        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
	            preparedStatement.setString(1, customer.getName());
	            preparedStatement.setString(2, customer.getEmail());
	            preparedStatement.setString(3, customer.getEventname());
	            preparedStatement.setString(4, customer.getEparticipant());
	            System.out.println(preparedStatement);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	    }

	    public customer selectUser(int id) {
	        customer user = null;
	        // Step 1: Establishing a Connection
	        try (Connection connection = getConnection();
	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
	            preparedStatement.setInt(1, id);
	            System.out.println(preparedStatement);
	            // Step 3: Execute the query or update query
	            ResultSet rs = preparedStatement.executeQuery();

	            // Step 4: Process the ResultSet object.
	            while (rs.next()) {
	                String name = rs.getString("name");
	                String email = rs.getString("email");
	                String eventname = rs.getString("eventname");
	                String eparticipant = rs.getString("eparticipant");
	                user = new customer(id, name, email, eventname,eparticipant);
	            }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        return user;
	    }

	    public List < customer > selectAllUsers() {

	        // using try-with-resources to avoid closing resources (boiler plate code)
	        List < customer > users = new ArrayList < > ();
	        // Step 1: Establishing a Connection
	        try (Connection connection = getConnection();

	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
	            System.out.println(preparedStatement);
	            // Step 3: Execute the query or update query
	            ResultSet rs = preparedStatement.executeQuery();

	            // Step 4: Process the ResultSet object.
	            while (rs.next()) {
	                int id = rs.getInt("id");
	                String name = rs.getString("name");
	                String email = rs.getString("email");
	                String eventname = rs.getString("eventname");
	                String eparticipant = rs.getString("eparticipant");
	                users.add(new customer(id, name, email, eventname,eparticipant));
	            }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        return users;
	    }

	    public boolean deleteUser(int id) throws SQLException {
	        boolean rowDeleted;
	        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
	            statement.setInt(1, id);
	            rowDeleted = statement.executeUpdate() > 0;
	        }
	        return rowDeleted;
	    }

	    public boolean updateUser(customer customer) throws SQLException {
	        boolean rowUpdated;
	        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
	            statement.setString(1, customer.getName());
	            statement.setString(2, customer.getEmail());
	            statement.setString(3, customer.getEventname());
	            statement.setString(4, customer.getEparticipant());
	            statement.setInt(5, customer.getId());

	            rowUpdated = statement.executeUpdate() > 0;
	        }
	        return rowUpdated;
	    }

	    private void printSQLException(SQLException ex) {
	        for (Throwable e: ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	
}
