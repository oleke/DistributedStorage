import java.util.ArrayList;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Master extends Remote {
    public void registerDatanode(Node n ) throws RemoteException;
    public Node getOneDataNode() throws RemoteException;
    public ArrayList<Node> getAllDataNode() throws RemoteException;
}
