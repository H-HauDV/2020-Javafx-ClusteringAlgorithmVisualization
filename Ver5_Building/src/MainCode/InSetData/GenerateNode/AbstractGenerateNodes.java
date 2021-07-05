package MainCode.InSetData.GenerateNode;

import MainCode.InSetData.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractGenerateNodes {
    public static final int MAX_Y = 400;
    public static final int MAX_X =800;
    ArrayList<int[]> customCenterArea;
    int[] numNodeForEachCluster;
    Node[] nodesTemp;
    double mutatingPercent;
    int number;
    double radius;
    int centerPoint;
    int optionForCenterPoint;
    public abstract void generateNodesSpecific();
    public abstract void customCenterPoint();
    public Node [] getNodeFromGenerateNodes(){
        return this.nodesTemp;
    }
    public void generateOneNode(int stt,int centerX,int centerY,int radius,Color color){
        nodesTemp[stt] = new Node();
        nodesTemp[stt].setCenterX(centerX);
        nodesTemp[stt].setCenterY(centerY);
        nodesTemp[stt].setRadius(radius);
        nodesTemp[stt].setFill(color);
    }
    public void mutatingDefault(int stt){
        Random r = new Random();
        nodesTemp[stt].setCenterX(r.nextInt(800));
        nodesTemp[stt].setCenterY(r.nextInt(400));
    }
    public abstract void mutatingSpecific(int stt);
}
