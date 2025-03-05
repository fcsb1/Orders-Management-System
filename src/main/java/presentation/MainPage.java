//de vazut functia aia populate table cum foloseste reflection
//de scris sql statemturile pt creare tabele

package presentation;

import businessLayer.BillLogic;
import dataAccessLayer.ProductDAO;
import model.Bill;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clasa principala din pachetul presentation
 * Ea reprezinta pagina de start a aplicatiei.
 * In ea se gaseste si metoda main
 * @author Mihai
 * @since 10.05.2024
 */
public class MainPage implements ActionListener
{
    public static final Font FONT = new Font("Serif", Font.BOLD, 20);
    private final static JPanel panel = new JPanel();
    private final static CardLayout paginiCard = new CardLayout();
    private final JButton btnClienti, btnProduse, btnComenzi,btnBill;
    private final ClientPage clientPage=new ClientPage();
    private final OrderPage orderPage=new OrderPage();
    private final ProductPage productPage=new ProductPage();
    private final BillPage billPage=new BillPage();

    private final JPanel mainPagePanel;

    public MainPage()
    {
        JFrame frame = new JFrame("Orders Management");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(0, 1));

        panel.setLayout(paginiCard);
        mainPagePanel = new JPanel();

        btnClienti = createButton("Clienti");
        btnProduse = createButton("Produse");
        btnComenzi = createButton("Comenzi");
        btnBill = createButton("Facturi");


        panel.add(mainPagePanel, "mainPage");
        panel.add(clientPage.getMainPanel(), "ClientPage");
        panel.add(productPage.getMainPanel(), "ProductPage");
        panel.add(orderPage.getMainPanel(), "OrderPage");
        panel.add(billPage.getMainPanel(), "BillPage");
        paginiCard.show(panel, "mainPage");

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static JPanel getPanel()
    {
        return panel;
    }

    public static CardLayout getPaginiCard()
    {
        return paginiCard;
    }

    public static void main(String[] args)
    {
         new MainPage();
    }

    private JButton createButton(String label)
    {
        JButton button = new JButton(label);
        button.setFont(FONT);
        button.addActionListener(this);
        mainPagePanel.add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnClienti)
        {
            paginiCard.show(panel, "ClientPage");
        } else if (e.getSource() == btnProduse)
        {
            ClientPage.populateTable(new ProductDAO().selectAll(), productPage.getTable());
            paginiCard.show(panel, "ProductPage");
        } else if (e.getSource() == btnComenzi)
        {
            paginiCard.show(panel, "OrderPage");
        } else if (e.getSource()==btnBill)
        {   ClientPage.populateTable(BillLogic.selectAll(),billPage.getTable());
            paginiCard.show(panel,"BillPage");
        }
    }
}