import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Scanner;

import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {
    private Master master;
    private Scanner scanner;

    public Client(int masterPort) throws Exception {
        this.connectToMaster(masterPort);
        this.scanner = new Scanner(System.in);
    }

    //Establish connection with the master node on the given port
    private void connectToMaster(int masterPort) throws Exception {
        // Getting the registry 
        Registry registry = LocateRegistry.getRegistry(null, masterPort); 
       
        // Looking up the registry for the remote object 
        this.master = (Master) registry.lookup("master"); 
    }

    // Invoke put data to get a list of registered nodes
    public void putData(int idData, String data) throws Exception {
        ArrayList<Node> nodes = this.master.getAllDataNode();
        if(!nodes.isEmpty()){
            System.out.println("List of registered nodes:");
            for(int i=0; i<nodes.size(); i++) {
                System.out.println(i + ". " + nodes.get(i).getHost() + ": " + nodes.get(i).getPort());
            }
            System.out.println("Enter a node id from the list above to put data");
            int input = scanner.nextInt();
            
            Node selectedNode = nodes.get(input);

            // Open up a socket on the selected DataNode
            this.openSocket(selectedNode.getPort(), "putData/"+idData+ "/"+ data);
        }
        else{
            System.out.println("No registered DataNode");
        }
    }

    //Invoke getData to get data from a random DataNode
    public void getData(int idData) throws Exception {
        Node dataNode = this.master.getOneDataNode();
        if(dataNode != null){
            System.out.println("Get from DataNode on port " + dataNode.getPort());
            this.openSocket(dataNode.getPort(), "getData/"+idData);
        }
        else {
            System.out.println("No registered DataNode");
        }
    }

    // Open a new socket on the given port with a message request to the socket
    private void openSocket(int port, String request) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), port);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(request);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());    
        String response = (String) ois.readObject();    
        System.out.println("Response from DataNode: " + response);
        socket.close();
    }
    public static void main(String[] args) throws Exception {
        Client c = new Client(8036);
        c.putData(0, "This is a dummy data");
        c.getData(0);
    }
}
