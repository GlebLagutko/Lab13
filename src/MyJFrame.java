import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class MyJFrame extends JFrame {

    private List<Product> productList = new ArrayList<>();
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
        fileChooser = new JFileChooser("C:\\Users\\Dell\\IdeaProjects\\Lab13\\src");
        JButton open = createOpenButton();
        buttonPanel.add(open);
        JButton edit = createEditButton();
        buttonPanel.add(edit);
        JButton add = createAddButton();
        buttonPanel.add(add);
        panel.add(buttonPanel, new
                GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.SOUTH,
                0, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(list, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
                1, new Insets(0, 0, 0, 0), 0, 0));
        return pane;
    }

    private JButton createAddButton() {
        JButton add = new JButton("Add");
        add.addActionListener(e -> {
            Product pr = new Product();
            new EditJDialog(this, "Add", pr);
            productList.add(pr);
            productList.sort(new MyComparator());
            show(productList);
        });
        return add;
    }

    private JButton createEditButton() {
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
                    new EditJDialog(this, "edit", productList.get(list.getSelectedIndex()));
                    show(productList);
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
            show(productList);

        });
        return open;
    }

    private void show(Collection<Product> a) {
        if (a != null) {
            listModel.clear();
            Iterator<Product> iter = productList.iterator();
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
            productList = new ArrayList<>();
            while (sc.hasNext())
                productList.add(new Product(sc.next(), sc.next(), sc.nextInt()));
            productList.sort(new MyComparator());
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(this, err, "Error!", JOptionPane.PLAIN_MESSAGE);
        } finally {
            if (sc != null)
                sc.close();
        }
    }
}
