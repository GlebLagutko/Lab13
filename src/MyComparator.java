import java.util.Comparator;

public class MyComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getCount() - o2.getCount() == 0 ? o1.getName().compareTo(o2.getName()) :
                o1.getCount() - o2.getCount();
    }
}
