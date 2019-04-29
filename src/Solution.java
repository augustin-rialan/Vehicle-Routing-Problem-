import java.util.ArrayList;

public class Solution {
    /* A solution is composed of a list of Route and of a cost */
    ArrayList<Route> listRoute;
    int cost=0;

    public ArrayList<Route> getlistRoute() {
        return listRoute;
    }

    public Solution(ArrayList<Route> list) {
        this.listRoute = new ArrayList<Route>(list);
    }

    public Solution(){
        this.listRoute=new ArrayList<Route>();
    }
    public void setCost(){
        this.cost=(int) Graph.solutionCost(this.listRoute);
    }
    public double getCost() {
        this.setCost();
        return cost;
    }

    public String toString() {
        String res = "";
        for (Route route : listRoute) {
            res = res + "\n";
            res = res + route.toString();
        }
        return res;
    }

    /* This methode code a solution to obtain a chromosome,
    we delete the nodes repository and we create an arraylist which contains all the nodes of a route
     */
    public Chromosome code() {
        Chromosome chromosome = new Chromosome();
        chromosome.setCost((int) this.getCost());

        for (Route route : this.listRoute) {
            for (Node node : route.getNodeList()) {
                if (node.getNodeID() !=1) {
                    chromosome.sequence.add(node.getNodeID());
                }
            }
        }
        return chromosome;

    }

    public void addRoute(Route route){
        this.listRoute.add(route);
    }




}