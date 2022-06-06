import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    private Socket socket = null;
    private ObjectInputStream inputObject;
    private ObjectOutputStream outputObject;

    public Server (int port) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            socket = serverSocket.accept();
            System.out.println("Client connected");

            outputObject = new ObjectOutputStream(socket.getOutputStream());
            inputObject = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()) {
                ServiceMessage msg = (ServiceMessage) inputObject.readObject();
                switch (msg.getMessageType()) {
                    case CLOSE_CONNECTION: {
                        closeConnection();
                        break;
                    }
                    case CLIENT_SEND: {
                        Thread thread = new Thread(() -> {
                            try {
                                receiveFile(msg);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        thread.start();
                    }
                    case CLIENT_RECEIVE: sendFile(msg);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(ServiceMessage msg) throws IOException{
        String fileName = msg.getFileName();
        String fileDir = "/Users/andrejdubovik/Desktop/";
//        String fileDir = "../FileTransferIO/ServerIO/src/main/resources/";
        int fileSize = msg.getFileSize();
        try (OutputStream fileOut = Files.newOutputStream(Paths.get(fileDir + fileName))) {
            int bytesRead;
            int totalRead = 0;
            int remainBytes = fileSize;
            byte[] byteBuffer = new byte[Math.min(fileSize, bufSize())];
            while (remainBytes > 0 && (bytesRead = inputObject.read(byteBuffer, 0, Math.min(remainBytes, byteBuffer.length))) != -1) {
                fileOut.write(byteBuffer, 0, bytesRead);
                totalRead += bytesRead;
                remainBytes -= bytesRead;
            }
            int progress = (fileSize - remainBytes) / fileSize * 100;
            System.out.println(progress + "% completed, received " + totalRead + " bytes");
        }
    }

    private void sendFile(ServiceMessage msg) {

    }

    private int bufSize() {
        // todo подумать над привязкой к размеру файла, количеству потоков итд
        return 1024;
    }

    private void closeConnection() {
        try {
            inputObject.close();
            outputObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
