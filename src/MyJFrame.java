import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyJFrame extends JFrame {

    private List<Product> myList;
    private JFileChooser fileChooser = new JFileChooser("C:/Users/Dell/IdeaProjects/Lab13");
    private FirstPanel firstPanel;
    private SecondPanel secondPanel;

    public MyJFrame() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            setSize(700, 500);
            MyJFrame.this.setLocationRelativeTo(null);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    System.exit(0);
                }
            });
        });
    }

    @Override
    public JRootPane createRootPane() {
        JRootPane pane = new JRootPane();
        JPanel panel = new JPanel();
        pane.setContentPane(panel);
        panel.setLayout(new BorderLayout());
        myList = new ArrayList<>();
        JMenuBar menuBar = new JMenuBar();
        JMenu submenu = new JMenu("Menu");
        menuBar.add(submenu);
        JMenuItem open = getOpenButton();
        submenu.add(open);
        JMenuItem add = getAddButton();
        submenu.add(add);
        JMenuItem save = getSaveButton();
        submenu.add(save);
        JTabbedPane tabbedPane = new JTabbedPane();
        firstPanel = new FirstPanel(myList);
        secondPanel = new SecondPanel(myList);
        tabbedPane.add("tab1", firstPanel);
        tabbedPane.add("tab2", secondPanel);
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(menuBar, BorderLayout.NORTH);
        return pane;
    }

    private JMenuItem getSaveButton() {
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            saveToXml();
            JOptionPane.showMessageDialog(this, "Done", "", JOptionPane.PLAIN_MESSAGE);
        });
        return save;
    }

    private JMenuItem getAddButton() {
        JMenuItem menuItem2 = new JMenuItem("Add");
        menuItem2.addActionListener(e -> {
            Product pr = new Product();
            new EditJDialog(this, "Add", pr);
            myList.add(pr);
            updateAll();
        });
        return menuItem2;
    }

    private JMenuItem getOpenButton() {
        JMenuItem menuItem1 = new JMenuItem("Open");
        menuItem1.addActionListener(e -> {
            try {
                fileChooser.setDialogTitle("Октрытие файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    DocumentBuilderFactory products = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = products.newDocumentBuilder();
                    Document document = builder.parse(fileChooser.getSelectedFile());
                    NodeList productElements = document.getDocumentElement().getElementsByTagName("product");
                    myList.clear();
                    for (int i = 0; i < productElements.getLength(); i++) {
                        Node product = productElements.item(i);
                        NamedNodeMap attributes = product.getAttributes();
                        myList.add(new Product(attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("country").getNodeValue(),
                                Integer.parseInt(attributes.getNamedItem("count").getNodeValue())));
                    }
                    updateAll();
                }
            } catch (SAXException | ParserConfigurationException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Problems with file", "Error!", JOptionPane.PLAIN_MESSAGE);
            }
        });
        return menuItem1;
    }

    private void updateAll() {
        firstPanel.update();
        secondPanel.update();
    }

    private void saveToXml() {
        try {
            fileChooser.setDialogTitle("Сохранение файла");
            fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
                try (FileWriter wr = new FileWriter(fileChooser.getSelectedFile())) {
                    wr.write("<?xml version=\"1.0\"?>" + "\n");
                    wr.write("<products>" + "\n");
                    Iterator<Product> iter = myList.iterator();
                    while (iter.hasNext())
                        wr.write(iter.next().toXML() + "\n");
                    wr.write("</products>");
                } catch (IOException ignored) {
                    JOptionPane.showMessageDialog(this, "Problems with file", "Error!", JOptionPane.PLAIN_MESSAGE);
                }
        } catch (Exception ignored) {

        }

    }
}
