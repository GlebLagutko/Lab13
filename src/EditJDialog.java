import javax.swing.*;
import java.awt.*;
import java.util.zip.DataFormatException;


public class EditJDialog extends  JDialog{
    private JButton ok = new JButton("Ok");
    private JLabel labelName = new JLabel("  Name:");
    private JLabel labelCountry = new JLabel("  Country:");
    private JLabel labelCount = new JLabel("  Count:");
    private JTextField inputName = new JTextField("", 5);
    private JTextField inputCountry = new JTextField("", 5);
    private JTextField inputCount = new JTextField();
    private Product product ;

    public EditJDialog(JFrame parent, String title, Product o) {
        super(parent, title, true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.product = o;

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(4, 2));

        container.add(labelName);
        inputName.setText(product.getName());
        container.add(inputName);

        container.add(labelCountry);
        inputCountry.setText(product.getCountry());
        container.add(inputCountry);

        container.add(labelCount);

        inputCount.setText(Integer.toString(product.getCount()));
        container.add(inputCount);
        createOkButton();
        container.add(ok);
        pack();
        setVisible(true);
    }

    private void createOkButton() {
        ok.addActionListener(e->{
            try {
                if (inputName.getText().equals(""))
                    throw new DataFormatException("Set name!");
                product.setName(inputName.getText());
                product.setCountry(inputCountry.getText());
                product.setCount(Integer.parseInt(inputCount.getText()));
                setVisible(false);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, err, "Error!", JOptionPane.PLAIN_MESSAGE);
            } catch (DataFormatException err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Error!", JOptionPane.PLAIN_MESSAGE);
            }
        }
        );
    }


}