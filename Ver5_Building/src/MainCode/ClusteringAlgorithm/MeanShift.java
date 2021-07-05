package MainCode.ClusteringAlgorithm;

import MainCode.Controller;
import MainCode.HelpfullFunction;
import MainCode.InSetData.GenerateNode.AbstractGenerateNodes;
import MainCode.InSetData.GenerateNode.GenerateCentroids;
import MainCode.InSetData.GenerateNode.MeanShiftGenerateNode;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import MainCode.InSetData.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MeanShift extends KMAndMSGeneral {
    private ArrayList<Node>[] nodeBelongToCircle;
    private Circle[] circleAroundCentroid;
    private int[] totalDistanceMoved;
    private int minPointArgument=0;
    private ArrayList<double[]> blackList;

    private void initTotalDistanceMoved(){
        totalDistanceMoved=new int[this.k];
        for(int i=0;i<this.k;i++){
            totalDistanceMoved[i]=0;
        }
    }
    private void printTotalDistanceMoved(){
        for(int i=0;i<this.k;i++){
            System.out.println(totalDistanceMoved[i]);
        }
        System.out.println("///////////////////////////////////////");
    }
    private void getNodesBelongToCircle(){
        nodeBelongToCircle= new ArrayList[this.k];
        for (int i = 0; i < this.k; i++) {
            nodeBelongToCircle[i] = new ArrayList<Node>();
        }
        for(int i=0;i<this.k;i++){
            for(int j=0;j<this.listOfNodes.size();j++){
                if(distanceOfTwoNodes(centroids[i],this.listOfNodes.get(j))<MS_CIRCLE_AROUND_CENTROIDS_RADIUS){
                    nodeBelongToCircle[i].add(this.listOfNodes.get(j));
                }
            }
        }
    }
    private void repositionCentroids1(){
        Random r= new Random();
        for (int i=0;i<this.k;i++){
            double sumCenterX=0;
            double sumCenterY=0;
            for(int j=0;j<nodeBelongToCircle[i].size();j++){
                sumCenterX+=nodeBelongToCircle[i].get(j).getCenterX();
                sumCenterY+=nodeBelongToCircle[i].get(j).getCenterY();
            }
            double averageCenterX;
            double averageCenterY;
            if(nodeBelongToCircle[i].size()<=1){
                int nodeTomoveTo= HelpfullFunction.getRandomIntInRange(0,this.listOfNodes.size());
                averageCenterX=this.listOfNodes.get(nodeTomoveTo).getCenterX()+HelpfullFunction.getRandomDoubleInRange(-10,10);
                averageCenterY=this.listOfNodes.get(nodeTomoveTo).getCenterY()+HelpfullFunction.getRandomDoubleInRange(-10,10);
//                averageCenterX=r.nextInt(800);
//                averageCenterY=r.nextInt(400);
            }else{
                averageCenterX=sumCenterX/nodeBelongToCircle[i].size();
                averageCenterY=sumCenterY/nodeBelongToCircle[i].size();
            }
            totalDistanceMoved[i]+=Math.sqrt(Math.pow(averageCenterX-centroids[i].getCenterX(),2)+Math.pow(averageCenterY-centroids[i].getCenterY(),2));
            nextXForVisual[i]=averageCenterX;
            nextYForVisual[i]=averageCenterY;
            centroids[i].setCenterX(averageCenterX);
            centroids[i].setCenterY(averageCenterY);
            nodeBelongToCircle[i].clear();
        }
    }
    private void repositionCentroids2(){//find new tetory
        Random r= new Random();
        for(int i=0;i<this.k;i++){
            for(int j=0;j<this.k;j++){
                if(j==i) continue;
                else{
                    while(distanceOfTwoNodes(centroids[i],centroids[j])<MS_CIRCLE_AROUND_CENTROIDS_RADIUS){
                        if(totalDistanceMoved[i]<totalDistanceMoved[j]){
                            int centerX=r.nextInt(800);
                            int centerY=r.nextInt(400);
                            centroids[j].setCenterX(centerX);
                            centroids[j].setCenterY(centerY);
                        }else{
                            int centerX=r.nextInt(800);
                            int centerY=r.nextInt(400);
                            centroids[i].setCenterX(centerX);
                            centroids[i].setCenterY(centerY);
                        }

                    }
                }
            }
        }
    }
    private void moveCentroids(){
        SequentialTransition stTemp=new SequentialTransition();
        for (int i=0;i<this.k;i++){
            TranslateTransition translateTransition=new TranslateTransition();
            translateTransition.setNode(centroidVisual[i]);
            translateTransition.setByX(nextXForVisual[i]-currentXForVisual[i]);
            translateTransition.setByY(nextYForVisual[i]-currentYForVisual[i]);
            translateTransition.setDuration(Duration.millis(Controller.SPEED));
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.setCycleCount(1);
            stTemp.getChildren().add(translateTransition);
            nodeBelongToCircle[i].clear();
            currentXForVisual[i]=nextXForVisual[i];
            currentYForVisual[i]=nextYForVisual[i];
        }
        st.add(stTemp);
    }
    private void initCircleForCentroids(Pane display){
        circleAroundCentroid=new Circle[this.k];
        for (int i=0;i<this.k;i++){
            circleAroundCentroid[i] = new Circle();
            circleAroundCentroid[i].setCenterX(currentXForVisual[i]);
            circleAroundCentroid[i].setCenterY(currentYForVisual[i]);
            circleAroundCentroid[i].setRadius(MS_CIRCLE_AROUND_CENTROIDS_RADIUS);
            circleAroundCentroid[i].setFill((Color.web("#000000",0)));
            display.getChildren().add(circleAroundCentroid[i]);
        }
    }
    private void showCircleForCentroids(){
        SequentialTransition stTemp=new SequentialTransition();
        for (int i=0;i<this.k;i++){
            stTemp.getChildren().add(colorShape(circleAroundCentroid[i],Color.web("#74b9ff",0.3)));
        }
        st.add(stTemp);
    }
    private void hideCircleForCentroids(){
        SequentialTransition stTemp=new SequentialTransition();
        for (int i=0;i<this.k;i++){
            FadeTransition trans = new FadeTransition();
            trans.setDuration(Duration.millis(Controller.SPEED));
            trans.setNode(circleAroundCentroid[i]);
            trans.setFromValue(1.0);
            trans.setToValue(0.0);
            trans.setAutoReverse(true);
            stTemp.getChildren().add(trans);
        }
        st.add(stTemp);
    }
    private int minPointArgumentCalculate(Node[] nodes){
        int[] allDistanceBetweenNodes=new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            allDistanceBetweenNodes[i] = 0;
        }
        for(int i=0;i<nodes.length;i++){
            for(int j=i+1;j<nodes.length;j++){
                if(distanceOfTwoNodes(nodes[i],nodes[j])<MS_CIRCLE_AROUND_CENTROIDS_RADIUS) allDistanceBetweenNodes[i]+=1;
            }
        }
        return Arrays.stream(allDistanceBetweenNodes).max().getAsInt();
    }
    public void initListOfNodes(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new MeanShiftGenerateNode(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes=new ArrayList<>();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes())) ;
    }
    public Node[] addSomeNodeToListOfNode(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new MeanShiftGenerateNode(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes()));
        return generateNodesTemp.getNodeFromGenerateNodes();
    }
    private void initCentroids() {
        centroids=new Node[this.k];
        GenerateCentroids generateCentroids = new GenerateCentroids(this.k,MS_CENTRAL_NODE_RADIUS);
        generateCentroids.generateCentroidsForMeanShift();
        centroids=new Node[generateCentroids.getCentroids().length];
        centroids = generateCentroids.getCentroids();
    }
    private void setXYForVisualCentroids(){
        for (int i=0;i<this.k;i++){
            centroids[i].setFill(Color.BLUE);
            currentXForVisual[i]=centroids[i].getCenterX();
            currentYForVisual[i]=centroids[i].getCenterY();
        }
    }
    @Override
    public SequentialTransition[] startSort(int k,Pane display) {
        st=new ArrayList<>();
        initK(k);
        initTotalDistanceMoved();
        initXYForVisual();
        initCentroids();
        setXYForVisualCentroids();
        drawCentroids(display);

        for(int i=0;i<MAX_RUN;i++) {
            initCircleForCentroids(display);
            showCircleForCentroids();
            getNodesBelongToCircle();
            repositionCentroids1();
            repositionCentroids2();
            //printTotalDistanceMoved();
            if(checkStopCondition()==1)
            {
               // System.out.println("Stop");
                break;
            }
            hideCircleForCentroids();
            moveCentroids();
        }
        return this.st.toArray(new SequentialTransition[0]);
    }
}
