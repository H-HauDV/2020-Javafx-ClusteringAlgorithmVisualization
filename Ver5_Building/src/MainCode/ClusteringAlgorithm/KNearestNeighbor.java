package MainCode.ClusteringAlgorithm;

import MainCode.Controller;
import MainCode.InSetData.GenerateNode.AbstractGenerateNodes;
import MainCode.InSetData.GenerateNode.GenerateCentroids;
import MainCode.InSetData.GenerateNode.KNearestNeighborGenerateNodes;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import MainCode.InSetData.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class KNearestNeighbor extends AbstractClustering {
    private Node centroids;
    private Node[] neighbors;
    private double[] distance;
    private Line[] lines;

    private void calculatingDistanceFromCentroidToNodes(){
        distance=new double[this.listOfNodes.size()];
        for (int i=0;i<this.listOfNodes.size();i++){
            distance[i]=distanceOfTwoNodes(centroids,this.listOfNodes.get(i));
        }
        Arrays.sort(distance);
        neighbors=new Node[this.k];
        for(int i=0;i<this.k;i++){
            for (int j=0;j<this.listOfNodes.size();j++){
                if(distanceOfTwoNodes(centroids,this.listOfNodes.get(j))==distance[i]){
                    neighbors[i]=this.listOfNodes.get(j);
                    break;
                }
            }
        }
    }
    private void initLines(Pane display){
        lines=new Line[this.k];
        for(int i=0;i<this.k;i++){
            lines[i] = new Line();
            lines[i].setStartX(centroids.getCenterX());
            lines[i].setStartY(centroids.getCenterY()-1000);
            lines[i].setEndX(neighbors[i].getCenterX());
            lines[i].setEndY(neighbors[i].getCenterY()-1000);
            lines[i].setStroke(neighbors[i].getFill());
            display.getChildren().add( lines[i]);
        }
    }
    private void showLines(){
        SequentialTransition stTemp=new SequentialTransition();
        for (int i=0;i<this.k;i++){
            TranslateTransition translateTransition=new TranslateTransition();
            translateTransition.setNode(lines[i]);
            translateTransition.setByX(0);
            translateTransition.setByY(1000);
            translateTransition.setDuration(Duration.millis(Controller.SPEED));
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.setCycleCount(1);
            stTemp.getChildren().add(translateTransition);
        }
        st.add(stTemp);
    }
    private void removeLines(Pane display){
        SequentialTransition stTemp=new SequentialTransition();
        for(int i=display.getChildren().size()-this.k;i<display.getChildren().size();i++){
            Line line= (Line) display.getChildren().get(i);

            FadeTransition trans = new FadeTransition(Duration.millis(Controller.SPEED), line);
            trans.setFromValue(1.0);
            trans.setToValue(00);
            //trans.setCycleCount(FadeTransition.INDEFINITE);
            trans.setAutoReverse(true);
            stTemp.getChildren().add(trans);
        }
        st.add(stTemp);
    }
    private void voting(){
//        System.out.println(distinctNodeType.get(0).getFill());
//        System.out.println(neighbors[0].getFill());
        HashMap<Paint,Integer> hashmap = new HashMap<Paint,Integer>();
        for (int j = 0; j < neighbors.length; j++) {
            hashmap.put(neighbors[j].getFill(), j);
        }
        //init vote result
        int[] voteResult=new int[hashmap.size()];
        for (int i = 0; i < voteResult.length; i++) {
            voteResult[i]=0;
        }
        //getting vote result
        for (int i = 0; i < hashmap.size(); i++) {
            for(int j=0;j<neighbors.length;j++){
                if(hashmap.keySet().toArray()[i]==neighbors[j].getFill()) voteResult[i]=voteResult[i]+1;
            }
        }
        int maxVote=voteResult[0];
        int maxVotePosition=0;
        for (int i = 1; i < voteResult.length; i++) {
            if(voteResult[i]>maxVote) maxVotePosition=i;
        }
        for (int i = 0; i < neighbors.length; i++) {
            System.out.println(neighbors[i].getFill());
        }
        SequentialTransition stTemp=new SequentialTransition();
        stTemp.getChildren().add(colorShape(centroids, (Color) hashmap.keySet().toArray()[maxVotePosition]));
        st.add(stTemp);
    }
    private void drawCentroids(Pane display){
        SequentialTransition stTemp=new SequentialTransition();
        display.getChildren().add(centroids);
        FadeTransition trans = new FadeTransition();
        trans.setDuration(Duration.millis(Controller.SPEED));
        trans.setNode(centroids);
        trans.setFromValue(0.0);
        trans.setToValue(1.0);
        trans.setAutoReverse(true);
        stTemp.getChildren().add(trans);
        st.add(stTemp);
    }




    public void initListOfNodes(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new KNearestNeighborGenerateNodes(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes=new ArrayList<>();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes()));
    }
    public Node[] addSomeNodeToListOfNode(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new KNearestNeighborGenerateNodes(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes()));
        return generateNodesTemp.getNodeFromGenerateNodes();
    }
    private void initCentroids() {
        centroids=new Node();
        GenerateCentroids generateCentroids = new GenerateCentroids(1,KNN_CENTRAL_NODE_RADIUS);
        generateCentroids.generateCentroidsForKNearestNeighbor();
        centroids = generateCentroids.getCentroids()[0];
        centroids.setFill(Color.web("#000000",0.7));
    }
    @Override
    public SequentialTransition[] startSort(int k,Pane display) {
        st=new ArrayList<>();
        initK(k);
        initCentroids();
        drawCentroids(display);
        calculatingDistanceFromCentroidToNodes();
        initLines(display);
        showLines();
        voting();
        removeLines(display);
        return st.toArray(new SequentialTransition[0]);
    }
}
