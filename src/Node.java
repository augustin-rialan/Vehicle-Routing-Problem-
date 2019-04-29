
import java.util.ArrayList;

public class Node {

    private int nodeID;
    private int posX;
    private int posY;
    private int demand;

    public Node(int i, int x, int y){
        nodeID = i;
        posX = x;
        posY = y;
    }

    public Node(Node other){
        this.nodeID = other.nodeID;
        this.posX = other.posX;
        this.posY = other.posY;
        this.demand = other.demand;
    }

    public int getPosX() {
        return posX;
    }

    public int getDemand() {
        return demand;
    }

    public int getPosY() {
        return posY;
    }

    public int getNodeID(){
        return nodeID;
    }

    public void SetDemand(int d){
        demand = d;
    }

    /*
    method calculating the distance from the current node to another node
     */
    public double getDistanceTo(Node n){

        return Math.sqrt(Math.pow((this.posX - n.getPosX()),2) + Math.pow((this.posY - n.getPosY()),2));
    }
}
