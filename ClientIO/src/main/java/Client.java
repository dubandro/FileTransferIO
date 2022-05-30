import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InputStream externalInput;
    private OutputStream externalOutput;
    public Client(String host, int port) {

        try {
            socket = new Socket(host, port);
            externalInput = socket.getInputStream();
            externalOutput = socket.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//            closeConnection();
        }
    }

    public void sendFile(String file) {
        File fileToSend = new File(file);
        try (DataInputStream readData = new DataInputStream(new FileInputStream(fileToSend));
                DataOutputStream sendData = new DataOutputStream(externalOutput)) {
            //Read
            String fileName = fileToSend.getName();
            String fileDir = fileToSend.getParent();
            int fileSize = (int)fileToSend.length();

            System.out.println("fileName = " + fileName + ", fileParent = " + fileDir + ", fileSize = " + fileSize);

            byte [] arrayData = new byte[fileSize];
            readData.readFully(arrayData, 0, fileSize);
            //Send
            sendData.writeUTF(fileName);
            sendData.writeInt(fileSize);
            sendData.write(arrayData, 0, fileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (!socket.isClosed()) {
            try {
                externalInput.close();
                externalOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
                if (socket.isClosed()) System.out.println("Socket close, client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            clientThread.interrupt();
        }
    }
}
