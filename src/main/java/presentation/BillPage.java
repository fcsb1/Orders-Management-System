package presentation;

import businessLayer.BillLogic;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static presentation.MainPage.FONT;

/**
 * Clasa folosita pentru afisarea facturilor
 * Ea reprezinta o pagina din GUI unde se pot vedea facturile
 *
 * @author Mihai
 * @since 10.05.2024
 */
public class BillPage implements ActionListener
{
    private final JPanel mainPanel;
    private final JButton backButton;
    private final JTable table;
    private final JPanel buttonPanel;

    public BillPage()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Facturi"));
        // Buttons Panel
        buttonPanel = new JPanel(new FlowLayout());

        backButton = createButton("Back");

        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Table Panel
        table = new JTable(new DefaultTableModel());
        JScrollPane tableScrollPane = new JScrollPane(table);

        ClientPage.populateTable(BillLogic.selectAll(), table);
        mainPanel.setPreferredSize(new Dimension(400, 400));
        tableScrollPane.setSize(400, 400);//.setPreferredScrollableViewportSize(mainPanel.getPreferredSize());
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
    }

    public JTable getTable()
    {
        return table;
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backButton)
            MainPage.getPaginiCard().show(MainPage.getPanel(), "mainPage");
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
