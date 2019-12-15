import javax.swing.*;
import java.util.Iterator;
import java.util.List;

public class FirstPanel extends JPanel {
    private List<Product> list;
    private JTextArea output;

    public FirstPanel(List<Product> model) {
        super();
        this.list = model;

        output = new JTextArea();
        this.add(output);
        this.update();
    }

    public void update() {
        output.setText("");
        Iterator<Product> iter = list.iterator();
        while (iter.hasNext()) {
            output.append(iter.next().toString() + "\n");
        }
    }
}