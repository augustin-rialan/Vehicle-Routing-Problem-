        import java.io.BufferedReader;
        import java.io.FileReader;
        import java.lang.reflect.Array;
        import java.lang.reflect.Executable;
        import java.util.ArrayList;
        import java.util.ArrayList;
        import java.io.*;
        import java.util.Collections;
        import java.util.Random;


        public class Chromosome {
            public ArrayList<Integer> sequence;
            public ArrayList<Node> nodelist = new ArrayList<Node>();
            public int cost;


            @Override
            public int hashCode() {
                return super.hashCode();
            }

            public Chromosome(ArrayList<Integer> chromosome){
                this.sequence=new ArrayList<Integer>(chromosome);


            }
            /* This method generate the list of all the clients (nodes), this is helpful for the method decode */
            public void nodeListGeneration() throws Exception{
                this.nodelist=new ArrayList<Node>();
                FileReader input = new FileReader("coordFile");
                BufferedReader bufRead = new BufferedReader(input);
                String myLine = null;

                while ( (myLine = bufRead.readLine()) != null)
                { String[] array1 = myLine.split("\t",5);
                    Node n = new Node(Integer.parseInt(array1[0]),Integer.parseInt(array1[1]),Integer.parseInt(array1[2]));
                    this.nodelist.add(n);

                }


            }
            /* this methode associate the demand with each node of the node list created with the method nodeListGeneration */
            public void generateDemands() throws Exception{
                FileReader input = new FileReader("demandFile");
                BufferedReader bufRead = new BufferedReader(input);
                String myLine = null;

                int i = 1;

                while ( (myLine = bufRead.readLine()) != null)
                {
                    String[] array1 = myLine.split("\t",5);
                    this.nodelist.get(Integer.parseInt(array1[0])-1).SetDemand(Integer.parseInt(array1[1]));
                    i++;
                }
            }




            public Chromosome(){
                this.sequence=new ArrayList<Integer>();
            }

            @Override
            public String toString() {
                return this.sequence.toString();
            }

            /* This method take as input a chromosome and create the solution associated */
            public Solution decode(){
            Solution solution=new Solution();
            try {
                this.nodeListGeneration();
                this.generateDemands();
            }
            catch (Exception e){
                e.getMessage();
            }

            /* We must add the node until the capacity is reached, moreover we need to begin and end
            each route by the repository node 1
             */
            int capacity= 206;
            Node currentNode = null;
            Route currentRoute= new Route();
                currentRoute.addNode(new Node(1,365,689));

                for (int nodeId : this.sequence){
                //we look for the node in the list with the nodeId
                for (Node node : this.nodelist){
                    if (node.getNodeID()==nodeId){
                        currentNode=node;
                    }
                }
                if (capacity > currentNode.getDemand()){
                    currentRoute.addNode(currentNode);
                    capacity = capacity - currentNode.getDemand();
                }
                //else if the capacity is too high we begin a new route
                else {
                    currentRoute.addNode(new Node(1,365,689));
                    solution.addRoute(currentRoute);
                    currentRoute = new Route();
                    currentRoute.addNode(new Node(1,365,689));
                    currentRoute.addNode(currentNode);
                    capacity=206-currentNode.getDemand();
                }

            }
                currentRoute.addNode(new Node(1,365,689));
                solution.addRoute(currentRoute);

                return solution;
            }

            public int getCost(){
                return this.cost;
            }
            public void setCost(int cost){
                 this.cost=cost;
            }
        }