package jm.task.core.jdbc.dao;

//import jdk.swing.interop.SwingInterOpUtils;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class UserDaoJDBCImpl   implements UserDao {
private final Connection connection = Util.getInstance().getConnection();


    public UserDaoJDBCImpl() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {
    }

    public void createUsersTable() throws SQLException {
        Statement statement = null;
        statement = connection.createStatement();
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255), lastName VARCHAR(255) NOT NULL , " +
                    "age int NOT NULL, PRIMARY KEY (ID))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Users CASCADE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveUser(String name, String lastName, byte age) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO Users(name, lastName, age) VALUES(?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в базу данных");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = (?)");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(userList);
        return userList;
    }

    public void cleanUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Users");

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}

