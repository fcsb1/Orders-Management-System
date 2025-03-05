package presentation;

import businessLayer.BillLogic;
import dataAccessLayer.ClientDAO;
import dataAccessLayer.OrderDAO;
import dataAccessLayer.ProductDAO;
import model.Bill;
import model.Orderr;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static presentation.MainPage.FONT;

/**
 * Clasa folosita pentru afisarea comenzilor
 * Ea reprezinta o pagina din GUI unde se pot vedea, adauga, sterge, edita clienti in baza de date
 * La crearea unei noi comenzi se adauga automat in baza de date si o factura
 *
 * @author Mihai
 * @since 10.05.2024
 */
public class OrderPage implements ActionListener
{
    private final JPanel mainPanel, buttonPanel;
    private final JButton backButton, addButton, deleteButton;
    private final JTable table;
    private final OrderDAO orderdao = new OrderDAO();

    public OrderPage()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Comenzi"));
        // Buttons Panel
        buttonPanel = new JPanel(new FlowLayout());

        backButton = createButton("Back");
        addButton = createButton("Add");
        deleteButton = createButton("Delete");

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Table Panel
        table = new JTable(new DefaultTableModel());
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        ClientPage.populateTable(orderdao.selectAll(), table);
        backButton.addActionListener(this);
    }

    public static void completeComboBox(List<?> myList, JComboBox<Integer> comboBox)
    {
        for (Object obj : myList)
        {
            try
            {
                Field idfield = obj.getClass().getDeclaredField("id");
                idfield.setAccessible(true);
                comboBox.addItem(idfield.getInt(obj));
            } catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    private void showAddOrderDialog()
    {
        // cream campurile de afisat
        JComboBox<Integer> clientIdComboBox = new JComboBox<>();
        JComboBox<Integer> productIdComboBox = new JComboBox<>();

        completeComboBox(new ClientDAO().selectAll(), clientIdComboBox);
        completeComboBox(new ProductDAO().selectAll(), productIdComboBox);

        JTextField quantityField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Id client:"));
        dialogPanel.add(clientIdComboBox);
        dialogPanel.add(new JLabel("Id produs:"));
        dialogPanel.add(productIdComboBox);
        dialogPanel.add(new JLabel("Cantitate:"));
        dialogPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Add order", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("adaugat");
            int quantity = Integer.parseInt(quantityField.getText());

            ProductDAO productdaoaux = new ProductDAO();
            Product product = productdaoaux.findById((int) productIdComboBox.getSelectedItem());

            if (quantity <= product.getStock())
            {
                product.setStock(product.getStock() - quantity);
                productdaoaux.update(product, product.getId());
                int noOrder = orderdao.getMaxNoOrder();
                orderdao.insert(new Orderr(noOrder + 1, (int) clientIdComboBox.getSelectedItem(), product.getId(), quantity, quantity * product.getPrice()));
                ClientPage.populateTable(orderdao.selectAll(), table);
                int noFactura = BillLogic.getMaxNoFactura() + 1;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                Bill newBill = new Bill(noFactura, LocalDateTime.now().format(formatter), (int) clientIdComboBox.getSelectedItem(), (int) productIdComboBox.getSelectedItem());
                BillLogic.insert(newBill);
            } else
                JOptionPane.showMessageDialog(null, "The quantity is greater than the available stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backButton)
            MainPage.getPaginiCard().show(MainPage.getPanel(), "mainPage");
        if (e.getSource() == deleteButton)
            if (table.getSelectedRow() != -1)
            {
                orderdao.delete((int) table.getValueAt(table.getSelectedRow(), 0));
                ClientPage.populateTable(orderdao.selectAll(), table);
            }
        if (e.getSource() == addButton)
        {
            showAddOrderDialog();
        }
    }

    private JButton createButton(String label)
    {
        JButton button = new JButton(label);
        button.setFont(FONT);
        button.addActionListener(this);
        mainPanel.add(button);
        return button;
    }
}
