public class appClient {

    private static final String HOST = "localhost";
    private static final int PORT = 65500;
    private static final String FILE = "../FileTransferIO/ClientIO/src/main/resources/file_text.txt";

    public static void main(String[] args) {
       Client client= new Client(HOST, PORT);
       client.sendFile(FILE);
    }
}
