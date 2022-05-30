import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    Socket socket = null;
    int bytesRead = 0;

    public Server (int port) {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            socket = serverSocket.accept();
            System.out.println("Client connected");
            DataInputStream inputData = new DataInputStream(socket.getInputStream());
            DataOutputStream outputData = new DataOutputStream(socket.getOutputStream());
            while (socket.isConnected() && bytesRead <= 0) {
                System.out.println("start reading...");
                String fileName = inputData.readUTF();
                String fileDir = "../FileTransferIO/ServerIO/src/main/resources"; //todo
                OutputStream fileOut = new FileOutputStream(fileDir + "/" + fileName);
                int fileSize = inputData.readInt();
                System.out.println("fileName = " + fileName + ", fileParent = " + fileDir + ", fileSize = " + fileSize);

                byte [] byteBuffer = new byte[1024];
                while (fileSize > 0 && (bytesRead = inputData.read(byteBuffer, 0, Math.min(fileSize, byteBuffer.length))) != -1) {
                    System.out.println("reading bytes = " + bytesRead);
                    System.out.println("memory buffer:\n" + new String(byteBuffer, "UTF-8"));
                    fileOut.write(byteBuffer, 0, bytesRead);
                    fileSize -= bytesRead;

                }
                socket.close();
                System.out.println("...end reading");
            }
            System.out.println("Close connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
