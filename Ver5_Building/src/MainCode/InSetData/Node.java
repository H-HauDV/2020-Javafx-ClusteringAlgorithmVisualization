package MainCode.InSetData;
import javafx.scene.shape.Circle;

public class Node extends Circle {
    private String nameOfNode;
    public Node() {
    }
    public void setNameOfNode(String nameNode){
        this.nameOfNode=nameNode;
    }
    public String getNameOfNode(String nameNode){
        return this.nameOfNode;
    }
}
