import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.util.Random;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataNode {
    private ServerSocket socket;
    private int masterPort;

    public DataNode(int masterPort){
        this.masterPort = masterPort;
    }

    // Initialize DataNode and open up socket to receive request from clients
    public void startDataNode() throws Exception {
        Random random = new Random();
        int port = random.nextInt(8200- 8100) + 8200;
        this.socket = new ServerSocket(port);
        registerDataNode(port);
        while (true) {
            System.out.println("Waiting for client on port " + this.socket.getLocalPort() + "..."); 
            this.openSocket();
        }
        
    }

    //Open up a socket to accept connection from clients
    private void openSocket() throws Exception{
        Socket client = this.socket.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);
        
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        String data = (String) ois.readObject();
        System.out.println("Request: " + data);
        this.parseRequest(data, client);
    }

    //Parse the recived request from the client and send back response to the open socket output stream
    private void parseRequest(String request, Socket socket) throws Exception {
        System.out.println(request);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        String[] body = request.split("/");
        if(request.startsWith("putData")){
            Storage.putData(Integer.parseInt(body[1]), body[2]);
            oos.writeObject("putData Invoked");
        }
        else if(request.startsWith("getData")){
            Storage.getData(Integer.parseInt(body[1]));
            oos.writeObject("getData Invoked");
        }
        else{
            oos.writeObject("Nothing Invoked");
        }
    }

    // Register DataNode on the master node
    private void registerDataNode(int port){
        try {  
            // Getting the registry 
            Registry registry = LocateRegistry.getRegistry(null,this.masterPort); 
       
            // Looking up the registry for the remote object 
            Master stub = (Master) registry.lookup("master"); 
       
            // Calling the remote method using the obtained object

            Node n = new Node(port, "localhost");

            stub.registerDatanode(n);
            
            System.out.println("Remote method registerNode invoked"); 
         } catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace();
         } 
    }

    public static void main(String[] args) throws Exception {
        DataNode dn = new DataNode(8036);
        dn.startDataNode();
    }
}