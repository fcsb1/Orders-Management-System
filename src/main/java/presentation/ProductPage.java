package presentation;

import dataAccessLayer.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static presentation.MainPage.FONT;

/**
 * Clasa folosita pentru afisarea produselor
 * Ea reprezinta o pagina din GUI unde se pot vedea, adauga, sterge, edita produse in baza de date
 *
 * @author Mihai
 * @since 10.05.2024
 */
public class ProductPage implements ActionListener
{
    private final JPanel mainPanel, buttonPanel;
    private final JButton backButton, addButton, editButton, deleteButton;
    private final JTable table;
    private final ProductDAO productdao = new ProductDAO();

    public ProductPage()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Produse"));
        // Buttons Panel
        buttonPanel = new JPanel(new FlowLayout());

        backButton = createButton("Back");
        addButton = createButton("Add");
        editButton = createButton("Edit");
        deleteButton = createButton("Delete");

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Table Panel
        table = new JTable(new DefaultTableModel());
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        ClientPage.populateTable(productdao.selectAll(), table);
        backButton.addActionListener(this);
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public JTable getTable()
    {
        return table;
    }

    private void showEditProductDialog(int row)
    {
        // Create dialog components
        JTextField stockField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField denumireField = new JTextField(10);
        JTextField idField = new JTextField(10);

        denumireField.setText(table.getValueAt(row, 1).toString());
        stockField.setText(table.getValueAt(row, 3).toString());
        priceField.setText(table.getValueAt(row, 2).toString());
        idField.setText(table.getValueAt(row, 0).toString());

        int idOld = Integer.parseInt(idField.getText());

        JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Stock:"));
        dialogPanel.add(stockField);
        dialogPanel.add(new JLabel("Price:"));
        dialogPanel.add(priceField);
        dialogPanel.add(new JLabel("Denumire:"));
        dialogPanel.add(denumireField);
        dialogPanel.add(new JLabel("id:"));
        dialogPanel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Edit produs", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("editat");
            Product newProduct = new Product(Integer.parseInt(idField.getText()), denumireField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(stockField.getText()));
            productdao.update(newProduct, idOld);
            ClientPage.populateTable(productdao.selectAll(), table);
        } else
            System.out.println("Renuntare");
    }

    private void showAddProductDialog()
    {
        JTextField stockField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField denumireField = new JTextField(10);
        JTextField idField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Stock:"));
        dialogPanel.add(stockField);
        dialogPanel.add(new JLabel("Price:"));
        dialogPanel.add(priceField);
        dialogPanel.add(new JLabel("Denumire:"));
        dialogPanel.add(denumireField);
        dialogPanel.add(new JLabel("id:"));
        dialogPanel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("ok pressed");
            Product newProduct = new Product(Integer.parseInt(idField.getText()), denumireField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(stockField.getText()));
            productdao.insert(newProduct);
            ClientPage.populateTable(productdao.selectAll(), table);
        } else
            System.out.println("renuntare");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backButton)
            MainPage.getPaginiCard().show(MainPage.getPanel(), "mainPage");

        if (e.getSource() == deleteButton)
        {
            productdao.delete((int) table.getValueAt(table.getSelectedRow(), 0));
            ClientPage.populateTable(productdao.selectAll(), table);
        }

        if (e.getSource() == editButton && table.getSelectedRow() != -1)
            showEditProductDialog(table.getSelectedRow());
        if (e.getSource() == addButton)
            showAddProductDialog();
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
