import java.util.ArrayList;
import java.util.Collections;

public class Route {

    private ArrayList<Node> nodeList;

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public Route(){
        nodeList = new ArrayList<>();
    }

    public Route(Route other){
        this.nodeList = new ArrayList<>(other.nodeList);
    }

    public void addNode(Node n){
        nodeList.add(n);
    }

    public boolean contains(Node n){
        if(nodeList.contains(n)) return true;
        else return false;
    }

    /*
    method calculating the cost of a route
     */
    public double getCost(){

        int result = 0;
        for (int i = 0; i < nodeList.size()-1 ; i++) {
            result += nodeList.get(i).getDistanceTo(nodeList.get(i+1));
        }
        return result;
    }

    public String toString(){
        String result = "";
        for (Node n : nodeList){
            result += " -> " + n.getNodeID();
        }
        result += " COST " + getCost();
        return result;
    }

    /*
    method swapping nodes inside a route
     */
    public void swapOptimization(){

        for (int i = 1; i < nodeList.size()-2 ; i++) {

            //creation of a candidate route, cloned from the current route
            Route candidate = new Route(this);


            //switching two nodes inside the route
            Collections.swap(candidate.nodeList, i, i + 1);

            //if the cost of the candidate is less than the cost of the main route, swap the same nodes in the main route
            if (candidate.getCost() < this.getCost()) {
                Collections.swap(this.nodeList, i, i + 1);
            }
        }
    }

    /*
    method calculating how much will be taken from the vehicle's load
     */
    public int numberOfProducts(){

        int res = 0;
        for (Node n : nodeList){
            res += n.getDemand();
        }
        return res;
    }


}
