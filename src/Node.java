import java.io.Serializable;

public class Node implements Serializable {
    private int port;
    private String host;

    public Node(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}
