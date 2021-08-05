import java.util.*;

public class MasterImpl implements Master{

    private Hashtable<Integer, Node> nodes;

    public MasterImpl() {
        this.nodes = new Hashtable<Integer, Node>();
    }
    
    public void registerDatanode(Node n) {
        this.nodes.put(this.nodes.size()+1, n);
        System.out.println("New node registered on port "+n.getPort());
    }

    public Node getOneDataNode(){ 
        if(!this.nodes.isEmpty()){
            Random random = new Random();
            return this.nodes.getOrDefault(random.nextInt(this.nodes.size()),this.nodes.get(1));
        }
        return null;
    }
    public ArrayList<Node> getAllDataNode(){
        return new ArrayList<Node>(this.nodes.values());
    }
}
