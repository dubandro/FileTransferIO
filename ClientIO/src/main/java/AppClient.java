public class AppClient {
    private static final String HOST = "localhost";
    private static final int PORT = 65500;
    public static void main(String[] args) {
        new Client(HOST, PORT);
    }
}
