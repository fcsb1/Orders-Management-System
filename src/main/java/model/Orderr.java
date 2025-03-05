package model;

/**
 * Clasa ce contine informatii legate de o comanda
 * @author Mihai
 * @since 10.05.2024
 */

public class Orderr
{
    private int noOrder;
    private int clientId;
    private int productId;
    private int quantity;
    private int price;


    public Orderr(int noOrder, int clientId, int productId, int quantity, int price)
    {
        this.noOrder = noOrder;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
Arr
    public Orderr()
    {
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }
    public int getNoOrder()
    {
        return noOrder;
    }

    public void setNoOrder(int noOrder)
    {
        this.noOrder = noOrder;
    }

    public int getClientId()
    {
        return clientId;
    }

    public void setClientId(int clientId)
    {
        this.clientId = clientId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

}
