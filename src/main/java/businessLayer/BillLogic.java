package businessLayer;

import dataAccessLayer.ConnectionDB;
import model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Este folosita pentru implementarea logica a operatiilor ce au legatura cu facturile(Bills)
 * Clasa contine metode pentru interactiunea cu baza de date cum ar fii:
 * selectAll folosita la afisarea facturilor
 * insert pentru introducerea in baza de date a unei noi facturii
 * getMaxNoFactura pentru aflarea ultimei facturi generate
 */
public class BillLogic
{
    public static List<Bill> selectAll()
    {
        Connection dbConnection = ConnectionDB.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;

        try
        {
            findStatement = dbConnection.prepareStatement("Select * from Bill");
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

    private static List<Bill> createObjects(ResultSet resultSet)
    {
        List<Bill> list = new ArrayList<>();
        try
        {
            while (resultSet.next())
            {
                int nrfact = resultSet.getInt(1);
                String data = resultSet.getString(2);
                int idClient = resultSet.getInt(3);
                int idProdus = resultSet.getInt(4);
                Bill newBill = new Bill(nrfact, data, idClient, idProdus);
                list.add(newBill);
            }

        } catch (SecurityException | IllegalArgumentException | SQLException e)
        {
            e.printStackTrace();
        }
        ConnectionDB.close(resultSet);
        return list;
    }

    public static int getMaxNoFactura()
    {
        int max;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT MAX(numarulFacturii) as maxFact FROM Bill;";
        try
        {
            connection = ConnectionDB.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            resultSet.next();
            max = resultSet.getInt("maxFact");
            return max;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            ConnectionDB.close(resultSet);
            ConnectionDB.close(statement);
            ConnectionDB.close(connection);
        }
    }

    public static void insert(Bill bill)
    {
        String query = "INSERT into Bill VALUES (" + bill.numarulFacturii() + ", '" + bill.data() + "' ," + bill.clientID() + ',' + bill.productID() + ");";
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

}