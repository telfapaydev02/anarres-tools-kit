public class Main {
    public static void main(String[] args) {
        System.out.println(Main.class.isAssignableFrom(Object.class));
        System.out.println(Object.class.isAssignableFrom(Main.class));
    }
}
