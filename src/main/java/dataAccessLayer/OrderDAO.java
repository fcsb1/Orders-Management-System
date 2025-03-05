package dataAccessLayer;

import model.Orderr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Clasa concreta ce extinde AbstractDAO
 *  Pe langa metodele din clasa parinte aceasta contine si o metoda ce are rolul de a gasi
 *  ultimul id al unei comenzi. Ea e folosita la crearea unei noi comenzi
 * @author Mihai
 * @since 10.05.2024
 */
public class OrderDAO extends AbstractDAO <Orderr>
{
    public int getMaxNoOrder()
    {
        int maxNoOrderr;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT MAX(noOrder) as noOrder FROM Orderr;";
        try
        {
            connection = ConnectionDB.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            resultSet.next();
            maxNoOrderr=resultSet.getInt("noOrder");
            return maxNoOrderr;
        }
        catch( SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally
        {
            ConnectionDB.close(resultSet);
            ConnectionDB.close(statement);
            ConnectionDB.close(connection);
        }
    }
}
