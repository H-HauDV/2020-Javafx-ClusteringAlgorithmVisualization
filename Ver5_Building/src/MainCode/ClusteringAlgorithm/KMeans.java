package MainCode.ClusteringAlgorithm;

import MainCode.HelpfullFunction;
import MainCode.InSetData.GenerateNode.AbstractGenerateNodes;
import MainCode.InSetData.GenerateNode.GenerateCentroids;
import MainCode.InSetData.GenerateNode.KMeansGenerateNodes;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import MainCode.Controller;
import MainCode.InSetData.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans extends KMAndMSGeneral {
    List<Node>[] clusteredNodes;
    List<Node>[] bestClustered;

    private void initBestCluster(){
        bestClustered = new List[this.k];
        for (int i = 0; i < this.k; i++) {
            bestClustered[i] = new ArrayList<>();
        }
    }
    private void initCluster(){
        clusteredNodes = new List[this.k];
        for (int i = 0; i < this.k; i++) {
            clusteredNodes[i] = new ArrayList<>();
        }
    }
    private void copyClusteredNodesToBestClusteredNodes(){
        for (int i=0;i<this.k;i++){
            bestClustered[i].clear();
            bestClustered[i].addAll(clusteredNodes[i]);
        }

    }
    private double totalVariationOfCluster(List<Node>[] cluster){
        double averageNodes=listOfNodes.size()/this.k;
        double sumVariation=0;
        for (int i=0;i<this.k;i++){
            sumVariation+=averageNodes/cluster[i].size();
        }
        return sumVariation/this.k;
    }
    private void addNodesToCluster(){
        for(int i=0;i<this.listOfNodes.size();i++){
            int minPosition=0;
            double minDistance=distanceOfTwoNodes(this.listOfNodes.get(i),centroids[0]);
            for(int j=1;j<this.k;j++){
                if(distanceOfTwoNodes(this.listOfNodes.get(i),centroids[j])<minDistance){
                    minDistance=distanceOfTwoNodes(this.listOfNodes.get(i),centroids[j]);
                    minPosition=j;
                }
            }
            clusteredNodes[minPosition].add(this.listOfNodes.get(i));
        }
    }
    private void recolorNodeInCluster(){
        SequentialTransition stTemp=new SequentialTransition();
        for(int i=0;i<this.k;i++){
            for(int j=0;j<clusteredNodes[i].size();j++){
                stTemp.getChildren().add(colorShape(clusteredNodes[i].get(j), (Color) centroids[i].getFill()));
            }
        }
        st.add(stTemp);
    }
    private void recolorNodeToBlack(){
        SequentialTransition stTemp=new SequentialTransition();
        for(int i=0;i<listOfNodes.size();i++){
            stTemp.getChildren().add(colorShape(listOfNodes.get(i),Color.BLACK));
        }
        st.add(stTemp);
    }
    private void recolorNodeInBestCluster(Pane display){
        SequentialTransition stTemp=new SequentialTransition();
        for(int i=0;i<this.k;i++){
            for(int j=0;j<bestClustered[i].size();j++){
                stTemp.getChildren().add(colorShape(bestClustered[i].get(j), (Color) centroids[i].getFill()));
            }
        }
        st.add(stTemp);
    }
    private void hideCentroids(){
        SequentialTransition stTemp=new SequentialTransition();
        for(int i=0;i<this.k;i++){
            stTemp.getChildren().add(colorShape(centroidVisual[i], Color.web("#ffffff",0)));
        }
        st.add(stTemp);
    }
    private void repositionCentroid(){
        Random r= new Random();
        for (int i=0;i<this.k;i++){
            double sumCenterX=0;
            double sumCenterY=0;
            for(int j=0;j<clusteredNodes[i].size();j++){
                sumCenterX+=clusteredNodes[i].get(j).getCenterX();
                sumCenterY+=clusteredNodes[i].get(j).getCenterY();
            }
            double averageCenterX;
            double averageCenterY;
            if(clusteredNodes[i].size()<=1){
                averageCenterX=currentXForVisual[i];
                averageCenterY=currentYForVisual[i];
            }else{
                averageCenterX=sumCenterX/clusteredNodes[i].size();
                averageCenterY=sumCenterY/clusteredNodes[i].size();
            }
            nextXForVisual[i]=averageCenterX;
            nextYForVisual[i]=averageCenterY;
            centroids[i].setCenterX(averageCenterX);
            centroids[i].setCenterY(averageCenterY);
        }
    }
    private void clearClusteredNodes(){
        for (int i=0;i<this.k;i++){
            clusteredNodes[i].clear();
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
            currentXForVisual[i]=nextXForVisual[i];
            currentYForVisual[i]=nextYForVisual[i];
        }
        st.add(stTemp);
    }
    public void initListOfNodes(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new KMeansGenerateNodes(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes=new ArrayList<>();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes()));
    }
    public Node[] addSomeNodeToListOfNode(int noOfNode,int dForGenerate, int optionForCenterPoint){
        AbstractGenerateNodes generateNodesTemp=new KMeansGenerateNodes(noOfNode,NORMAL_NODE_RADIUS,dForGenerate,optionForCenterPoint);
        generateNodesTemp.generateNodesSpecific();
        this.listOfNodes.addAll(Arrays.asList(generateNodesTemp.getNodeFromGenerateNodes()));
        return generateNodesTemp.getNodeFromGenerateNodes();
    }
    private void initCentroids(){
        GenerateCentroids generateCentroids =new GenerateCentroids(this.k,KM_CENTRAL_NODE_RADIUS);
        generateCentroids.generateCentroidsForKMeans();
        this.centroids=new Node[generateCentroids.getCentroids().length];
        this.centroids= generateCentroids.getCentroids();
    }
    private void setXYForVisualCentroids(){
        for (int i=0;i<this.k;i++){
            currentXForVisual[i]=centroids[i].getCenterX();
            currentYForVisual[i]=centroids[i].getCenterY();
        }
    }
    protected void repositionCentroidsVisual(){
        for (int i=0;i<this.k;i++){
            double centerX= HelpfullFunction.getRandomDoubleInRange(50,750);
            double centerY=HelpfullFunction.getRandomDoubleInRange(50,350);
            nextXForVisual[i]=centerX;
            nextYForVisual[i]=centerY;
            centroids[i].setCenterX(centerX);
            centroids[i].setCenterY(centerY);
        }
    }
    @Override
    public SequentialTransition[] startSort(int k,Pane display){
        int maxLoop=5;
        st=new ArrayList<>();
        initK(k);
        initCentroids();
        initXYForVisual();
        setXYForVisualCentroids();

        initBestCluster();
        initCluster();
        drawCentroids(display);
        for (int l=0;l<maxLoop;l++){
            for (int i=0;i<MAX_RUN;i++){
                System.out.printf("Loop: %d\n",i);
                addNodesToCluster();
                recolorNodeInCluster();
                System.out.println(totalVariationOfCluster(clusteredNodes));
                repositionCentroid();
                if(checkStopCondition()==1){
                    //System.out.println("Stop");
                    break;
                }else{
                    clearClusteredNodes();
                }
                moveCentroids();
            }
            if(Math.abs(totalVariationOfCluster(clusteredNodes)-1)<Math.abs(totalVariationOfCluster(bestClustered)-1)){
                copyClusteredNodesToBestClusteredNodes();
            }
            if(l!=maxLoop-1) {
                clearClusteredNodes();
                recolorNodeToBlack();
                repositionCentroidsVisual();
                moveCentroids();
            }
        }
        hideCentroids();
        recolorNodeInBestCluster(display);
//        System.out.println(totalVariationOfCluster(bestClustered));
//        System.out.println(bestClustered[0].size());
        return st.toArray(new SequentialTransition[0]);
    }
}
