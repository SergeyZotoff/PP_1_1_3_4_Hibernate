package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

;import javax.transaction.Transactional;

public class UserDaoHibernateImpl implements UserDao {

    private static Session session;
    private Transaction transaction = null;

    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        session = Util.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("CREATE TABLE IF NOT EXISTS Users " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(255), lastName VARCHAR(255) NOT NULL , " +
                "age tinyint NOT NULL, PRIMARY KEY (ID))").executeUpdate();
        session.close();
    }


    @Override
    public void dropUsersTable() {
       session = Util.getSessionFactory().openSession();
       session.beginTransaction();
       session.createSQLQuery("DROP TABLE IF EXISTS Users CASCADE").executeUpdate();
       session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {

    }

    @Override
    public List<User> getAllUsers() {

         session = Util.getSessionFactory().openSession();
        Query query = session.createQuery("from User");
        List userList = query.getResultList();
         userList.forEach(System.out::println);
        session.close();
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        session = Util.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM Users").executeUpdate();
        session.close();
    }
}
