import java.io.Serializable;

public class ServiceMessage implements Serializable {
    private MessageType messageType;
    private String fileName;
    private String parentDir;
    private int fileSize;

    public ServiceMessage() {
    }

    public ServiceMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public ServiceMessage(MessageType messageType, String fileName, String parentDir, int fileSize) {
        this.messageType = messageType;
        this.fileName = fileName;
        this.parentDir = parentDir;
        this.fileSize = fileSize;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
