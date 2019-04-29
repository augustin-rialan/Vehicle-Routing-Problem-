        import java.util.*;

        public class AlgoGen {
            ArrayList<Chromosome> population;
            /* An algoGen has an initial population associated
            to it
             */
            public AlgoGen(ArrayList<Chromosome> population) {
                this.population = population;
            }

            public AlgoGen(){
                this.population=new ArrayList<Chromosome>();
            }

            /* This methode execute the genetic algorithm */
            public Solution execute() throws Exception {
                /*
                We begin by generation the initial population
                 */
                System.out.println("Initial population, generation 0");
                System.out.println("----------------------------------------------\n");
                for (int i = 0; i < 100; i++) {
                    Graph testGraph = new Graph();
                    testGraph.generateGraph();
                    testGraph.generateDemands();

                    Solution sol= testGraph.buildInitialSolution();
                   System.out.println("Solution number " + i + ": COST: " + sol.getCost());
                    Chromosome chro = sol.code();
                    population.add(chro);



                }

                /* We configure the algorithm for 100
                generations
                 */
                int generation=100;
                ArrayList<Chromosome> childrenList = new ArrayList<Chromosome>();
                while (generation>0) {


                    childrenList.removeAll(childrenList);
                    /*
                    We cross each individual with every other individual of
                    the population. Each crossing add two children in the childrenList
                     */
                    for (Chromosome parent1 : this.population) {
                        for (Chromosome parent2 : this.population) {
                            Chromosome children1 = this.crossing(parent1, parent2);
                            Chromosome children2 = this.crossing(parent2, parent1);
                            children1.setCost(((int) children1.decode().getCost()));
                            childrenList.add(children1);
                            children2.setCost(((int) children2.decode().getCost()));
                            childrenList.add(children2);
                            // we finally add the parents to the children list
                            childrenList.add(parent1);
                            childrenList.add(parent2);

                        }
                    }
                    /* We eliminate the duplicates in childrenList
                     */
                    Set set = new HashSet() ;
                    set.addAll(childrenList) ;
                    childrenList = new ArrayList(set) ;

                    /*
                    We calculate the cost of each children and put it in a list that
                    we sort
                     */
                    ArrayList<Integer> costList = new ArrayList<Integer>();
                    for (Chromosome chromosome : childrenList) {
                        costList.add((int) chromosome.getCost());


                    }

                    Collections.sort(costList);

                    /*
                    In the list bestCost we keep the 50 best cost
                     */
                    ArrayList<Integer> bestCost = new ArrayList<Integer>();
                    for (int i = 0; i < 50; i++) {
                        bestCost.add(costList.get(i));

                    }

                    /*
                    Using the list bestCost we do the list of the 50 best chromosomes:
                    finalChildrenList
                     */
                    ArrayList<Chromosome> finalChildrenList = new ArrayList<Chromosome>();
                    for (int bestcost : bestCost) {
                        for (Chromosome child : childrenList) {
                            int costChildrenList = (int) child.getCost();
                            if (costChildrenList == bestcost) {
                                finalChildrenList.add(child);
                                break;
                            }
                        }
                    }
                    int generationNumber=101-generation;
                    System.out.println("Generation number: " + generationNumber);
                    System.out.println("----------------------------------------------\n");
                    int i = 0;
                    population = new ArrayList<Chromosome>();
                    /* we add each individual of the
                    list finalChildrenList to the new population
                     */
                    for (Chromosome chro : finalChildrenList) {
                        Solution firstSol=chro.decode();
                        Graph.hillClimb(firstSol);
                        chro=firstSol.code();
                        population.add(chro);

                    }
                    /*
                    We generate 50 individuals that we optimize and add to the population
                     */
                    for ( i=50;i<100;i++){
                        Graph testGraph = new Graph();
                        testGraph.generateGraph();
                        testGraph.generateDemands();
                        Solution sol= testGraph.buildInitialSolution();
                        Graph.hillClimb(sol);

                        population.add(i,sol.code());
                    }

                    /*
                    We print all the solutions of the new population
                     */
                    for (i =0;i<100;i++){
                        System.out.println("Solution number " + i + " COST: " + population.get(i).decode().getCost());

                    }
                    generation--;
                }
                /*
                At the last generation, we find the best individual
                and print it
                 */
                Solution bestSolution= population.get(0).decode();
                for (Chromosome child : population){
                    if (child.getCost()<bestSolution.getCost()){
                        bestSolution=child.decode();
                    }
                }
                System.out.println("BEST SOLUTION: \n" + bestSolution.toString() + " \n\n TOTAL COST OF THE SOLUTION: " + bestSolution.getCost());

                return bestSolution;
            }

            public Chromosome crossing(Chromosome parent1, Chromosome parent2){
                Chromosome child=new Chromosome();
                /*
                We initialize the child sequence of nodes
                 */
                for (int i=0;i<100;i++){
                    child.sequence.add(i,0);
                }
                Random rand=new Random();
                int crossingpoint1= rand.nextInt(100);
                int crossingpoint2= rand.nextInt(100);
                if (crossingpoint2 < crossingpoint1) {
                    int aux= crossingpoint1;
                    crossingpoint1=crossingpoint2;
                    crossingpoint2=aux;

                }

                //we use the technic of the Order Crossover (OX) for crossing the parents

                //first all the nodes between cp1 and cp2 from the parent1 are copied to the child
                for (int i=crossingpoint1; i<crossingpoint2; i++){
                    child.sequence.set(i, parent1.sequence.get(i));
                }
                //then we add all the other node of parents1 but in the same order than in parent2
                //for this we begin to do a list of all the other node of parents1
                ArrayList<Integer> leftover = new ArrayList<Integer>();
                for (int i=1; i<101; i++){
                    leftover.add(i-1,i+1);
                }
                for (int node : child.sequence){
                    for (int i=0;i<leftover.size();i++){
                        if (leftover.get(i)==node) {
                            leftover.set(i,-1);
                        }
                    }
                }
                //now we add all the elements from leftover to children 1 in the same order than in parent2
                int count=0;

                for (int a = 0; a < crossingpoint1; a++) {
                    while (!leftover.contains(parent2.sequence.get(count))) {
                        count++;
                    }
                    child.sequence.set(a, parent2.sequence.get(count));
                    leftover.set(leftover.indexOf(parent2.sequence.get(count)),-1);
                }

                for (int b = crossingpoint2; b < 100; b++) {
                    while (!leftover.contains(parent2.sequence.get(count))) {
                        count++;
                    }
                    child.sequence.set(b, parent2.sequence.get(count));
                    leftover.set(leftover.indexOf(parent2.sequence.get(count)),-1);
                }



                return child;
            }


        }
