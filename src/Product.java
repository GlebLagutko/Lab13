import java.util.Objects;

public class Product {
    private String name;
    private String country;
    private int count;

    public Product() {
    }

    public Product(String name, String country, int count) {
        this.name = name;
        this.country = country;
        this.count = count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return count == product.count &&
                Objects.equals(country, product.country) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, name, count);
    }

    @Override
    public String toString() {
        return  "country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count ;
    }
}
