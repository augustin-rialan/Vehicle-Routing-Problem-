
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.Random;


public class Graph {

    //nodes contained in the graph, each node represents a client
    private ArrayList<Node> nodes;



    public Graph(){
        nodes = new ArrayList<>();
    }

    /*
    method for printing out all nodes and their coordinates
     */
    public void printNodes(){
        for (Node n : nodes){
            System.out.println("node number " + n.getNodeID() + " ( " + n.getPosX() + " , " + n.getPosY() + " ) ");
        }
    }

    /*
    method to print out all customer demands
     */
    public void printDemands(){
        for (Node n : nodes){
            System.out.println("customer number " + n.getNodeID() + " needs " + n.getDemand());
        }
    }

    /*
    method that parses the file containing the nodes coordinates and adds them to the graph
     */
    public void generateGraph() throws Exception{

        FileReader input = new FileReader("coordFile");
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = null;

        while ( (myLine = bufRead.readLine()) != null)
        {

            String[] array1 = myLine.split("\t",5);
            Node n = new Node(Integer.parseInt(array1[0]),Integer.parseInt(array1[1]),Integer.parseInt(array1[2]));
            nodes.add(n);
        }
    }

    /*
    method that parses the file containing the demands and adds them to their corresponding customer
     */
    public void generateDemands() throws Exception{
        FileReader input = new FileReader("demandFile");
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = null;

        while ( (myLine = bufRead.readLine()) != null)
        {
            String[] array1 = myLine.split("\t",5);
            nodes.get(Integer.parseInt(array1[0])-1).SetDemand(Integer.parseInt(array1[1]));
        }
    }


    /*
    method calculating the total cost of a solution
     */
    public static double solutionCost(ArrayList<Route> solution){

        double totalCost = 0;
        for (Route route : solution){
            totalCost += route.getCost();
        }

        return totalCost;
    }


    /*
    method optimizing a solution through the internal swapping method
     */
    public static void optimizeSolutionSwap(Solution solution){
        for (Route r : solution.listRoute){
            if (r.getNodeList().size()>4){
                r.swapOptimization();
            }
        }
    }

    /*
    method optimizing a solution through the inter-route switching method
     */
    public static void optimizeSolutionSwitch(Solution solution){

        for (int indexRoute = 0; indexRoute < solution.getlistRoute().size()-1; indexRoute++) {
            for (int indexNode = 1; indexNode < solution.getlistRoute().get(indexRoute).getNodeList().size() -1 ; indexNode++) {
                for (int indexNode2 = 1; indexNode2 < solution.getlistRoute().get(indexRoute + 1).getNodeList().size() -1 ; indexNode2++) {

                    //oldCost is the cost of the solution before switching the nodes
                    double oldCost = solution.getCost();

                    //n1 and n2 are the nodes that will be switched
                    Node n1 = new Node(solution.getlistRoute().get(indexRoute).getNodeList().get(indexNode));
                    Node n2 = new Node(solution.getlistRoute().get(indexRoute + 1).getNodeList().get(indexNode2));

                    //switching of the nodes
                    solution.getlistRoute().get(indexRoute).getNodeList().set(indexNode,n2);
                    solution.getlistRoute().get(indexRoute + 1).getNodeList().set(indexNode2,n1);

                    //newCost is the cost of the solution after swapping
                    double newCost = solution.getCost();


                    //if the total cost has increased or the demand is too high, the nodes are swapped back
                    if(oldCost<newCost || solution.getlistRoute().get(indexRoute).numberOfProducts()>206 || solution.getlistRoute().get(indexRoute+1).numberOfProducts()>206){

                        solution.listRoute.get(indexRoute).getNodeList().set(indexNode,n1);
                        solution.listRoute.get(indexRoute + 1).getNodeList().set(indexNode2,n2);
                    }
                }
            }
        }
    }


    /*
    method generating the initial solution that will be optimized
     */
    public Solution buildInitialSolution() {

        // nodesToVisit contains all the nodes that have not been visited
        ArrayList<Node> nodesToVisit = new ArrayList<>(nodes);

        ArrayList<Route> initialSolution = new ArrayList<Route>();
        Random rand = new Random();

        //startNode is the depot, where every route begins and ends
        Node startNode = nodesToVisit.get(0);
        nodesToVisit.remove(startNode);
        boolean fail=false;
        Node nextNode=null;
        while (nodesToVisit.size() > 0) {

            //creation of new route, adding of the start node (depot node number 1) and removal of the start node from the nodes to visit
            Route route = new Route();
            route.addNode(startNode);
            int capacity=206;
            while (true) {

                //if there are no more nodes to visit, exit the loop
                if (nodesToVisit.size() == 0){
                    break;
                }

                if (!fail) {
                    nextNode = nodesToVisit.get(rand.nextInt(nodesToVisit.size()));
                }

                fail=false;

                /*
                if the demand of the next node does not exceed the remaining number of products in the vehicle,
                the node is visited and the demand is fulfilled
                 */
                int demand=nextNode.getDemand();
                if (demand < capacity) {
                    route.addNode(nextNode);
                    nodesToVisit.remove(nextNode);
                    capacity = capacity - demand;


                }
                else {
                    fail=true;
                    route.addNode(startNode);
                    break;
                }
            }

            if (nodesToVisit.size()==0){
                route.addNode(startNode);
            }

            initialSolution.add(route);

        }
        //creation of the solution and adding of the routes
        Solution solution = new Solution();
        for (Route route : initialSolution){
            solution.addRoute(route);
        }
        return solution;
    }

    /*
    method improving a solution through the hill climbing method
     */
    public static void hillClimb(Solution sol){
        sol.setCost();
        //currentScore is the cost of the solution before the optimizing sweep
        double currentScore = solutionCost(sol.getlistRoute());

        //call of the switch and swap optimizing methods
        optimizeSolutionSwitch(sol);
        optimizeSolutionSwap(sol);
        sol.setCost();

        //currentScore is the cost of the solution after the optimizing sweep
        double newScore = solutionCost(sol.getlistRoute());

        //if the score has improved, do another sweep
        if (newScore<currentScore){
            hillClimb(sol);
        }
    }




    public static void main(String[] args) throws Exception{


    AlgoGen algoGen=new AlgoGen();
    algoGen.execute();



    }


}
