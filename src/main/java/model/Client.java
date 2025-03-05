package model;

/**
 * Clasa ce contine informatii legate de un client
 * @author Mihai
 * @since 10.05.2024
 */
public class Client
{
    private int id;
    private String nume;
    private String tel;
    private String adresa;

    public Client(int id, String nume, String tel, String adresa)
    {
        this.id = id;
        this.nume = nume;
        this.tel = tel;
        this.adresa = adresa;
    }

    public int getId()
    {
        return id;
    }

    public String getNume()
    {
        return nume;
    }

    public String getTel()
    {
        return tel;
    }

    public String getAdresa()
    {
        return adresa;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setNume(String nume)
    {
        this.nume = nume;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setAdresa(String adresa)
    {
        this.adresa = adresa;
    }

    public Client()
    {
    }
}
