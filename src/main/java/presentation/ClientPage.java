package presentation;

import dataAccessLayer.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static presentation.MainPage.FONT;

/**
 * Clasa folosita pentru afisarea clientilor
 * Ea reprezinta o pagina din GUI unde se pot vedea, adauga, sterge, edita clienti in baza de date
 * @author Mihai
 * @since 10.05.2024
 */
public class ClientPage implements ActionListener
{
    private final JPanel mainPanel;
    private final JButton backButton, addButton, editButton, deleteButton;
    private final JTable table;
    private final ClientDAO clientdao = new ClientDAO();


    public ClientPage()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Clienti"));
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

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

        populateTable(clientdao.selectAll(), table);
        tableScrollPane.setSize(400, 600); // Modifică dimensiunea preferată a panelului cu tabela
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Setează politica barei de derulare verticală

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.setPreferredSize(new Dimension(800, 600));
    }

    public static void populateTable(List<?> objectList, JTable table)
    {
        if (objectList.isEmpty())// Check if the list is empty
        {
            table.setModel(new DefaultTableModel());
            return;
        }

        // Get the class of the objects in the list
        Class<?> listItemClass = objectList.get(0).getClass();

        // Extract field names using reflection
        Field[] fields = listItemClass.getDeclaredFields();
        ArrayList<String> columnNames = new ArrayList<>();
        for (Field field : fields)
            columnNames.add((field.getName()));

        // Populate table model with column names
        DefaultTableModel model = new DefaultTableModel(columnNames.toArray(), 0);

        // Populate table with data from list
        for (Object obj : objectList)
        {
            ArrayList<Object> row = new ArrayList<>();
            for (Field field : fields)
            {
                field.setAccessible(true);
                try
                {
                    row.add(field.get(obj));
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
            model.addRow(row.toArray());
        }
        table.setModel(model);// Set table model
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    private void showAddClientDialog()
    {
        // Create dialog components
        JTextField numeField = new JTextField(10);
        JTextField adresaField = new JTextField(10);
        JTextField telField = new JTextField(10);
        JTextField idField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Nume:"));
        dialogPanel.add(numeField);
        dialogPanel.add(new JLabel("Adresa:"));
        dialogPanel.add(adresaField);
        dialogPanel.add(new JLabel("Tel:"));
        dialogPanel.add(telField);
        dialogPanel.add(new JLabel("id:"));
        dialogPanel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Add Client", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("adaugat");
            Client newClient = new Client(Integer.parseInt(idField.getText()), numeField.getText(), telField.getText(), adresaField.getText());
            clientdao.insert(newClient);

            populateTable(clientdao.selectAll(), table);

        } else
            System.out.println("renuntare");
    }

    private void showEditCLientDialog(int row)
    {
        // Create dialog components
        JTextField numeField = new JTextField(10);
        JTextField adresaField = new JTextField(10);
        JTextField telField = new JTextField(10);
        JTextField idField = new JTextField(10);

        numeField.setText(table.getValueAt(row, 1).toString());
        adresaField.setText(table.getValueAt(row, 3).toString());
        telField.setText(table.getValueAt(row, 2).toString());
        idField.setText(table.getValueAt(row, 0).toString());
        int idOld = Integer.parseInt(idField.getText());
        JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Nume:"));
        dialogPanel.add(numeField);
        dialogPanel.add(new JLabel("Adresa:"));
        dialogPanel.add(adresaField);
        dialogPanel.add(new JLabel("Tel:"));
        dialogPanel.add(telField);
        dialogPanel.add(new JLabel("id:"));
        dialogPanel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Edit Client", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
            System.out.println("editat");
            Client newClient = new Client(Integer.parseInt(idField.getText()), numeField.getText(), telField.getText(), adresaField.getText());
            clientdao.update(newClient, idOld);
            populateTable(clientdao.selectAll(), table);
        } else
            System.out.println("renuntare");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backButton)
            MainPage.getPaginiCard().show(MainPage.getPanel(), "mainPage");
        if (e.getSource() == addButton)
            showAddClientDialog();
        if (e.getSource() == deleteButton)
        {
            if (table.getSelectedRow() != -1)
                clientdao.delete((int) table.getValueAt(table.getSelectedRow(), 0));
            populateTable(clientdao.selectAll(), table);
        }
        if (e.getSource() == editButton)
        {
            if (table.getSelectedRow() != -1)
                showEditCLientDialog(table.getSelectedRow());
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
