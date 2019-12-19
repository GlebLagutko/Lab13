public class Main {
    public static void main(String[] args) {

        Object name = "fsdfs";
        Object country = "fds";
        Object count="fdsfsd";
        System.out.println(String.format("<product name = '%s' >"  + "\n" +
                        "<country>" + "%s" + "</country>" + "\n" + "<count>" + "%s" + "</count>" + "\n" + "</product>"
                , name, country, count));
        new MyJFrame();
    }
}
