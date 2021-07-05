package MainCode.ClusteringAlgorithm;

import MainCode.Controller;
import MainCode.InSetData.Node;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class KMAndMSGeneral extends AbstractClustering {
    protected Node[] centroids;
    protected double[] currentXForVisual;
    protected double[] currentYForVisual;
    protected double[] nextXForVisual;
    protected double[] nextYForVisual;
    protected Circle[] centroidVisual;

    protected void drawCentroids(Pane display){
        SequentialTransition stTemp=new SequentialTransition();
        centroidVisual=new Circle[this.k];
        for (int i=0;i<this.k;i++){
            centroidVisual[i] = new Circle();
            centroidVisual[i].setCenterX(currentXForVisual[i]);
            centroidVisual[i].setCenterY(currentYForVisual[i]);
            centroidVisual[i].setRadius(KM_CENTRAL_NODE_RADIUS);
            centroidVisual[i].setFill(centroids[i].getFill());
            display.getChildren().add(centroidVisual[i]);
            FadeTransition trans = new FadeTransition();
            trans.setDuration(Duration.millis(Controller.SPEED));
            trans.setNode(centroidVisual[i]);
            trans.setFromValue(0.0);
            trans.setToValue(1.0);
            trans.setAutoReverse(true);
            stTemp.getChildren().add(trans);
        }
        st.add(stTemp);
    }
    protected int checkStopCondition(){
        int returnValue=1;
        for(int i=0;i<this.k;i++){
            if(currentXForVisual[i]!=nextXForVisual[i] || currentYForVisual[i]!=nextYForVisual[i]){
                returnValue= 0;
                break;
            }
        }
        return returnValue;
    }
    protected void initXYForVisual(){
        currentXForVisual=new double[this.k];
        currentYForVisual=new double[this.k];
        nextXForVisual=new double[this.k];
        nextYForVisual=new double[this.k];
    }
    public void initListOfNodes(int noOfNode, int dForGenerate, int optionForCenterPoint) { }
    public abstract SequentialTransition[] startSort( int k,Pane display);
}
