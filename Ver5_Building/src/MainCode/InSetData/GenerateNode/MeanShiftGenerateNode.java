package MainCode.InSetData.GenerateNode;

import MainCode.HelpfullFunction;
import MainCode.InSetData.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class MeanShiftGenerateNode extends AbstractGenerateNodes {
    @Override
    public void customCenterPoint(){
        customCenterArea =new ArrayList<>();
        if(optionForCenterPoint==1){
            switch (centerPoint){
                case 1:
                    customCenterArea.add(new int[]{250,200}) ;
                case 2:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
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
                case 1:
                    customCenterArea.add(new int[]{250,200}) ;
                case 2:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
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
                case 1:
                    customCenterArea.add(new int[]{250,200}) ;
                case 2:
                    customCenterArea.add(new int[]{250,200}) ;
                    customCenterArea.add(new int[]{500,300}) ;
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
        customCenterPoint();
        Random r = new Random();
        numNodeForEachCluster= HelpfullFunction.getRandomArray(number,centerPoint);
        int currentNode=0;
        for (int i = 0; i < centerPoint; i++) {
            for(int j=0;j<numNodeForEachCluster[i];j++){
                nodesTemp[currentNode] = new Node();
                nodesTemp[currentNode].setRadius(radius);
                nodesTemp[currentNode].setFill(Color.BLACK);
                double centerX=customCenterArea.get(i)[0]-(int)(-90+(90-(-90)) * Math.random());
                double centerY=customCenterArea.get(i)[1]-(int)(-90+(90-(-90)) * Math.random());
                nodesTemp[currentNode].setCenterX(centerX);
                nodesTemp[currentNode].setCenterY(centerY);
                mutatingSpecific(currentNode);
                currentNode++;
            }
        }
    }
    public void centeringMeanShift(){
        Random r= new Random();
        int centerArgument=r.nextInt(nodesTemp.length/2);
        for (int i=0;i<centerArgument;i++){
            int centerX= customCenterArea.get(0)[0]-(int)(-40+(100-(-40)) * Math.random());
            int centerY= customCenterArea.get(0)[1]-(int)(-60+(75-(-60)) * Math.random());
            nodesTemp[i].setCenterX(centerX);
            nodesTemp[i].setCenterY(centerY);
        }
    }
    public MeanShiftGenerateNode(int number, int radius, int centerPoint, int optionForCenterPoint) {
        this.number=number;
        this.radius=radius;
        this.centerPoint=centerPoint;
        this.optionForCenterPoint=optionForCenterPoint;
    }

}
