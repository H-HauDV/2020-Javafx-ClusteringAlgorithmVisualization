package MainCode.InSetData.GenerateNode;

import MainCode.HelpfullFunction;
import MainCode.InSetData.Node;
import javafx.scene.paint.Color;

import java.util.Random;

public class GenerateCentroids{
    Node[] nodesTemp;
    int number;
    double radius;
    public static final int MAX_X=800;
    public static final int MAX_Y=400;
    public void generateCentroidsForKMeans(){
        this.nodesTemp=new Node[number];
        Random r = new Random();
        for (int i = 0; i < number; i++) {
            nodesTemp[i] = new Node();
            double centerX= HelpfullFunction.getRandomDoubleInRange(50,750);
            double centerY=HelpfullFunction.getRandomDoubleInRange(50,350);
            nodesTemp[i].setCenterX(centerX);
            nodesTemp[i].setCenterY(centerY);
            nodesTemp[i].setRadius(radius);
            Color colorForCentroids;
            switch (i){
                case 0:colorForCentroids=Color.web("#8e44ad",1.0);break;//purple
                case 1:colorForCentroids=Color.web("#16a085",1.0);break;//Green
                case 2:colorForCentroids=Color.web("#f39c12",1.0);break;//Orange
                case 3:colorForCentroids=Color.web("#c0392b",1.0);break;//Brown
                case 4:colorForCentroids=Color.web("#4cd137",1.0);break;//Weak Green
                case 5:colorForCentroids=Color.web("#192a56",1.0);break;//Strong Blue
                case 6:colorForCentroids=Color.web("#487eb0",1.0);break;//Weak Blue
                case 7:colorForCentroids=Color.web("#ff7979",1.0);break;//Pink
                case 8:colorForCentroids=Color.web("#f6e58d",1.0);break;//Yellow
                case 9:colorForCentroids=Color.web("#f9ca24",1.0);break;//Strong Yellow
                default:colorForCentroids=Color.BLACK;
            }
            nodesTemp[i].setFill(colorForCentroids);
        }
    }
    public void generateCentroidsForKNearestNeighbor(){
        this.nodesTemp=new Node[number];
        for (int i = 0; i < number; i++) {
            nodesTemp[i] = new Node();
            double centerX= HelpfullFunction.getRandomDoubleInRange(50,750);
            double centerY=HelpfullFunction.getRandomDoubleInRange(50,350);
            nodesTemp[i].setCenterX(centerX);
            nodesTemp[i].setCenterY(centerY);
            nodesTemp[i].setRadius(radius);
            Color colorForCentroids=Color.BLACK;
            nodesTemp[i].setFill(colorForCentroids);
        }
    }
    public void generateCentroidsForMeanShift(){
        this.nodesTemp=new Node[number];
        for (int i = 0; i < number; i++) {
            nodesTemp[i] = new Node();
            double centerX= HelpfullFunction.getRandomDoubleInRange(50,750);
            double centerY=HelpfullFunction.getRandomDoubleInRange(50,350);
            nodesTemp[i].setCenterX(centerX);
            nodesTemp[i].setCenterY(centerY);
            nodesTemp[i].setRadius(radius);
            Color colorForCentroids=Color.BLACK;
            nodesTemp[i].setFill(colorForCentroids);
        }
    }
    public GenerateCentroids(int number, int radius) {
        this.number=number;
        this.radius=radius;
    }
    public Node[] getCentroids(){
        return this.nodesTemp;
    }
}
