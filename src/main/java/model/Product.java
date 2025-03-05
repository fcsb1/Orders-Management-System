package model;

/**
 * Clasa ce contine informatii legate de un produs
 *
 * @author Mihai
 * @since 10.05.2024
 */
public class Product
{
    private int id;
    private String denumire;
    private int price;
    private int stock;

    public Product()
    {
    }

    public Product(int id, String denumire, int price, int stock)
    {
        this.id = id;
        this.denumire = denumire;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String toString()
    {
        return "Product{" +
                "id=" + id +
                ", denumire='" + denumire + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDenumire()
    {
        return denumire;
    }

    public void setDenumire(String denumire)
    {
        this.denumire = denumire;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

}
