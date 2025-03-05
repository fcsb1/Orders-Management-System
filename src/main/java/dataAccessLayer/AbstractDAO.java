package dataAccessLayer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aceasta este o clasa abstracta ce implementeaza metode pentru a accesa baza de date (CRUD operations).
 * Aceste metode folosesc tehnica reflection pentru obtinerea informatiilor necesare.
 *
 * @param <T> Tipul clasei cu care lucreaza metodele.
 * @author Mihai
 * @since 10.05.2024
 */
public class AbstractDAO<T>
{
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class type;

    public AbstractDAO()
    {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String insertQuery(T newObject)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT into ");
        sb.append(type.getSimpleName());
        sb.append(" values(");
        for (Field field : newObject.getClass().getDeclaredFields())
        {
            try
            {
                field.setAccessible(true);
                if (field.get(newObject) instanceof String)
                {
                    sb.append("'");
                    sb.append(field.get(newObject));
                    sb.append("'");
                } else
                    sb.append(field.get(newObject));
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);// stergem ultima virgula
        sb.append(");");
        return sb.toString();
    }

    public List<T> selectAll()
    {
        Connection dbConnection = ConnectionDB.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;

        try
        {
            findStatement = dbConnection.prepareStatement("Select * from " + type.getSimpleName());
            rs = findStatement.executeQuery();
            return createObjects(rs);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            ConnectionDB.close(rs);
            ConnectionDB.close(findStatement);
            ConnectionDB.close(dbConnection);
        }
    }

    public T findById(int id)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + type.getSimpleName() + " where id= " + id;
        try
        {
            connection = ConnectionDB.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e)
        {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally
        {
            ConnectionDB.close(resultSet);
            ConnectionDB.close(statement);
            ConnectionDB.close(connection);
        }
        return null;
    }


    private List<T> createObjects(ResultSet resultSet)
    {
        List<T> list = new ArrayList<>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++)
        {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try
        {
            while (resultSet.next())
            {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields())
                {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | InvocationTargetException | IntrospectionException | SQLException |
                 SecurityException | IllegalAccessException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        ConnectionDB.close(resultSet);
        return list;
    }

    public void insert(T newObject)
    {
        String query = insertQuery(newObject);
        System.out.println("the query is:" + query);
        Connection dbConnection = ConnectionDB.getConnection();
        PreparedStatement findStatement = null;

        try
        {
            findStatement = dbConnection.prepareStatement(query);
            findStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            ConnectionDB.close(dbConnection);
            ConnectionDB.close(findStatement);
        }
    }

    private String deleteQuery(int id)
    {
        StringBuilder st = new StringBuilder();
        st.append("DELETE from ").append(type.getSimpleName()).append(" where ").append(type.getDeclaredFields()[0].getName()).append(" = ").append(id).append(';');
        System.out.println(st);
        return st.toString();
    }

    public void delete(int id)
    {
        String query = deleteQuery(id);
        Connection dbConnection = ConnectionDB.getConnection();
        PreparedStatement findStatement = null;

        try
        {
            findStatement = dbConnection.prepareStatement(query);
            findStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            ConnectionDB.close(dbConnection);
            ConnectionDB.close(findStatement);
        }
    }

    private String updateQuery(T deEditat, int id)
    {
        StringBuilder st = new StringBuilder();
        st.append("UPDATE ").append(deEditat.getClass().getSimpleName()).append(" SET ");
        for (Field field : deEditat.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            try
            {
                if (field.get(deEditat) instanceof String)
                    st.append(field.getName()).append(" = '").append(field.get(deEditat)).append("',");
                else
                    st.append(field.getName()).append(" = ").append(field.get(deEditat)).append(",");

            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        st.deleteCharAt(st.length() - 1);
        st.append(" where id= ").append(id).append(';');
        return st.toString();
    }

    public void update(T deEditat, int id)// in deEditat avem cum va arata in final si in id avem
    {                                       // idul obiectului ce trebe editat
        String query = updateQuery(deEditat, id);
        System.out.println(query);
        Connection dbConnection = ConnectionDB.getConnection();
        PreparedStatement findStatement = null;

        try
        {
            findStatement = dbConnection.prepareStatement(query);
            findStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            ConnectionDB.close(dbConnection);
            ConnectionDB.close(findStatement);
        }
    }
}
