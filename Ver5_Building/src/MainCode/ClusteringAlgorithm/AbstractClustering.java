package MainCode.ClusteringAlgorithm;

import MainCode.InSetData.Node;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import MainCode.Controller;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractClustering {
    public final Color FINAL_COLOR = Color.web("0xe53935",1.0);
    public static int NORMAL_NODE_RADIUS=4;
    public static int KM_CENTRAL_NODE_RADIUS=7;
    public static int MS_CENTRAL_NODE_RADIUS=7;
    public static int KNN_CENTRAL_NODE_RADIUS=15;
    public static int MS_CIRCLE_AROUND_CENTROIDS_RADIUS=80;
    protected int k;
    protected ArrayList<SequentialTransition> st;
    protected static int MAX_RUN=30;
    private FillTransition f;
    private ParallelTransition p;
    protected ArrayList<Node> listOfNodes;

    protected void initK(int k){
        this.k=k;
    }
    public abstract void initListOfNodes(int noOfNode,int dForGenerate, int optionForCenterPoint);
    public abstract Node[] addSomeNodeToListOfNode(int noOfNode,int dForGenerate, int optionForCenterPoint);
    public abstract SequentialTransition[] startSort(int k,Pane display);

    public void removeSomeNodeInListOfNode(int numOfNodeToRemove){
        int sumNodesInListOfNodes=listOfNodes.size();
        for (int i=1;i<=numOfNodeToRemove;i++){
            listOfNodes.remove(sumNodesInListOfNodes-i);
        }
    }
    public void overrideListOfNodes(Node[] nodes){
        listOfNodes.clear();
        listOfNodes.addAll(Arrays.asList(nodes));
    }
    public ArrayList<Node> getListOfNodes(){
        return this.listOfNodes;
    }
    public void addANodeToListOfNodes(Node nodes){
        this.listOfNodes.add(nodes);
    }
    public void addNodesToListOfNodes(Node[] nodes){
        this.listOfNodes.addAll(Arrays.asList(nodes));
    }

    protected void fillTransionShape(Shape s, Color color) {
        f = new FillTransition();
        f.setShape(s);
        f.setToValue(color);
        f.setDuration(Duration.millis(Controller.SPEED));
        p.getChildren().add(f);
    }
    ParallelTransition colorShape(Shape s, Color color) {
        p = new ParallelTransition();
        fillTransionShape(s, color);
        return p;
    }
    protected double distanceOfTwoNodes(Node node1,Node node2){
        double distance=0;
        distance=Math.sqrt(Math.pow(node1.getCenterX()-node2.getCenterX(),2)+Math.pow(node1.getCenterY()-node2.getCenterY(),2));
        return distance;
    }
    public void printListOfNodes(){
        System.out.println("There is: "+listOfNodes.size() +"nodes.");
        for (int i=0;i<listOfNodes.size();i++){
            System.out.printf("%f, %f;\n",listOfNodes.get(i).getCenterX(),listOfNodes.get(i).getCenterY());
        }
        System.out.println("/////////////////////////");
    }
}
