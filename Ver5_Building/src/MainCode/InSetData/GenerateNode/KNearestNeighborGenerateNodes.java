package MainCode.InSetData.GenerateNode;

import MainCode.HelpfullFunction;
import MainCode.InSetData.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class KNearestNeighborGenerateNodes extends AbstractGenerateNodes {
    public Color[] colorForNodes;
    @Override
    public void customCenterPoint(){
        customCenterArea =new ArrayList<>();
        if(optionForCenterPoint==1){
            switch (centerPoint){
                case 3:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    break;
                case 4:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                case 5:
                    customCenterArea.add(new int[]{270,100}) ;
                    customCenterArea.add(new int[]{550,260}) ;
                    customCenterArea.add(new int[]{600,120}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                    customCenterArea.add(new int[]{600,300}) ;
            }
        }else if(optionForCenterPoint==2){
            switch (centerPoint){
                case 3:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    break;
                case 4:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                case 5:
                    customCenterArea.add(new int[]{270,100}) ;
                    customCenterArea.add(new int[]{550,260}) ;
                    customCenterArea.add(new int[]{600,120}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                    customCenterArea.add(new int[]{600,300}) ;
            }
        }else if(optionForCenterPoint==3){
            switch (centerPoint){
                case 3:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    break;
                case 4:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
                    customCenterArea.add(new int[]{650,150}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                case 5:
                    customCenterArea.add(new int[]{270,100}) ;
                    customCenterArea.add(new int[]{550,260}) ;
                    customCenterArea.add(new int[]{600,120}) ;
                    customCenterArea.add(new int[]{400,100}) ;
                    customCenterArea.add(new int[]{600,300}) ;
            }
        }
    }
    @Override
    public void mutatingSpecific(int stt) {

    }

    @Override
    public void generateNodesSpecific() {
        this.nodesTemp=new Node[number];
        generateColorForCluster();
        customCenterPoint();
        Random r = new Random();
        numNodeForEachCluster= HelpfullFunction.getRandomArray(number,centerPoint);
        int currentNode=0;
        for (int i = 0; i < centerPoint; i++) {
            for(int j=0;j<numNodeForEachCluster[i];j++){
                nodesTemp[currentNode] = new Node();
                nodesTemp[currentNode].setRadius(radius);
                nodesTemp[currentNode].setFill(colorForNodes[i]);
                double centerX=customCenterArea.get(i)[0]-(int)(-90+(90-(-90)) * Math.random());
                double centerY=customCenterArea.get(i)[1]-(int)(-90+(90-(-90)) * Math.random());
                nodesTemp[currentNode].setCenterX(centerX);
                nodesTemp[currentNode].setCenterY(centerY);
                mutatingSpecific(currentNode);
                currentNode++;
            }
        }
    }
    public void generateColorForCluster(){
        colorForNodes=new Color[centerPoint];
        for(int i=0;i<this.centerPoint;i++){
            switch (i) {
                case 0: colorForNodes[i] = Color.web("#8e44ad", 1.0);break;
                case 1: colorForNodes[i] = Color.web("#16a085",1.0);break;//Green
                case 2: colorForNodes[i] = Color.web("#f39c12",1.0);break;//Green
                case 3: colorForNodes[i] = Color.web("#c0392b",1.0);break;//Green
                case 4: colorForNodes[i] = Color.web("#4cd137",1.0);break;//Green
            }
        }
    }
    public KNearestNeighborGenerateNodes(int number, int radius, int centerPoint, int optionForCenterPoint) {
        this.number=number;
        this.radius=radius;
        this.centerPoint=centerPoint;
        this.optionForCenterPoint=optionForCenterPoint;
    }
}
