import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class MyJFrame extends JFrame {

    private TreeSet<Product> productSet;
    private DefaultListModel listModel;
    private JList list;
    private JFileChooser fileChooser;


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
        panel.setLayout(new GridBagLayout());
        listModel = new DefaultListModel();
        list = new JList(listModel);
        JPanel buttonPanel = new JPanel();
        productSet = new TreeSet<>(new MyComparator());
        fileChooser = new JFileChooser("C:\\Users\\Dell\\IdeaProjects\\Lab13");
        JButton open = createOpenButton();
        buttonPanel.add(open);
        JButton edit = createEditButton();
        buttonPanel.add(edit);
        JButton add = createAddButton();
        buttonPanel.add(add);
        JButton save = createSaveToXmlButton();
        buttonPanel.add(save);
        JButton openXML = createOpenXMLButton();
        buttonPanel.add(openXML);
        panel.add(buttonPanel, new
                GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.SOUTH,
                0, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(list, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
                1, new Insets(0, 0, 0, 0), 0, 0));
        return pane;
    }

    private JButton createOpenXMLButton() {
        JButton readXML = new JButton("OpenXML");
        readXML.addActionListener(e -> {
            try {
                fileChooser.setDialogTitle("Октрытие файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "xml");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    DocumentBuilderFactory products = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = products.newDocumentBuilder();
                    Document document = builder.parse(fileChooser.getSelectedFile());
                    NodeList productElements = document.getDocumentElement().getElementsByTagName("product");
                    productSet = new TreeSet<>(new MyComparator());
                    for (int i = 0; i < productElements.getLength(); i++) {
                        Node product = productElements.item(i);
                        NamedNodeMap attributes = product.getAttributes();
                        productSet.add(new Product(attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("country").getNodeValue(),
                                Integer.parseInt(attributes.getNamedItem("count").getNodeValue())));
                    }
                    show(productSet);
                }
            } catch (Exception ex) {
            }
        });
        return readXML;
    }

    private JButton createSaveToXmlButton() {
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            saveToXml();
            JOptionPane.showMessageDialog(this, "Done", "", JOptionPane.PLAIN_MESSAGE);
        });
        return save;
    }

    private JButton createAddButton() {
        JButton add = new JButton("Add");
        add.addActionListener(e -> {
            Product pr = new Product();
            new EditJDialog(this, "Add", pr);
            productSet.add(pr);
            show(productSet);
        });
        return add;
    }

    private JButton createEditButton() {
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
                    if (list.getSelectedIndex() >= 0) {
                        Iterator<Product> iter = productSet.iterator();
                        for (int i = 0; i < list.getSelectedIndex(); i++) {
                            iter.next();
                        }
                        new EditJDialog(this, "edit", iter.next());
                        show(productSet);
                    }
                }
        );
        return edit;
    }

    private JButton createOpenButton() {
        JButton open = new JButton("Open");
        open.addActionListener(e -> {
            fileChooser.setDialogTitle("Октрытие файла");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
                read(fileChooser.getSelectedFile());
            show(productSet);

        });
        return open;
    }

    private void show(Collection<Product> a) {
        if (a != null) {
            listModel.clear();
            Iterator<Product> iter = productSet.iterator();
            while (iter.hasNext())
                listModel.addElement(iter.next().toString());
            list.setSelectedIndex(0);
        } else
            JOptionPane.showMessageDialog(this, "There are no elements!", "Error!", JOptionPane.PLAIN_MESSAGE);
    }


    private void read(File file) {
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            productSet = new TreeSet<>(new MyComparator());
            while (sc.hasNext())
                productSet.add(new Product(sc.next(), sc.next(), sc.nextInt()));
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(this, err, "Error!", JOptionPane.PLAIN_MESSAGE);
        } finally {
            if (sc != null)
                sc.close();
        }
    }

    private void saveToXml() {
        try (FileWriter wr = new FileWriter("xmlFileName.xml")) {
            wr.write("<?xml version=\"1.0\"?>" + "\n");
            wr.write("<products>" + "\n");
            Iterator<Product> iter = productSet.iterator();
            while (iter.hasNext())
                wr.write(iter.next().toXML() + "\n");
            wr.write("</products>");
        } catch (IOException ex) {

        }
    }
}
