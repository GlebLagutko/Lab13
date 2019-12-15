import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SecondPanel extends JPanel {
    private List<Product> list;
    private JTextArea output;

    public SecondPanel(List<Product> model) {
        super();
        this.list = model;

        output = new JTextArea();
        this.add(output);
        this.update();
    }

    public void update() {
        output.setText("");
        List<Product> temp = new ArrayList<>(list);
        Collections.sort(temp);
        Iterator<Product> iter = temp.iterator();
        while (iter.hasNext()) {
            output.append(iter.next().toString() + "\n");
        }
    }
}