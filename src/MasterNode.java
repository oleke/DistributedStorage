import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject; 

public class MasterNode {
    private int port;

    public MasterNode(int port) {
        this.port = port;
    }

    public void start(){
        try{
            MasterImpl masterObj = new MasterImpl();

            Master api = (Master) UnicastRemoteObject.exportObject(masterObj, this.port);

            // Binding the remote object (stub) in the registry 
            Registry registry = LocateRegistry.createRegistry(this.port); 
         
            registry.bind("master", api);  
            System.err.println("Server ready on port: "+ this.port);
        }
        catch(Exception e){
            System.err.println("Server exception: " + e.toString()); 
            e.printStackTrace(); 
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        MasterNode server = new MasterNode(8036);
        server.start();
    }
}
