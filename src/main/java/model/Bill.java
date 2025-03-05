package model;

/**
 * Clasa de tip record ce reprezinta factura generata pentru fiecare comanda.
 *
 * @param numarulFacturii identificator unic pentru fiecare factura (numar intreg)
 * @param data            data la care s-a facturat
 * @param clientID        id cumparatorului
 * @param productID       id produsului cumparat
 */
public record Bill(int numarulFacturii, String data, int clientID, int productID)
{

}