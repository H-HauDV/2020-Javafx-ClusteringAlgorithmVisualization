package MainCode;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import MainCode.ClusteringAlgorithm.KMeans;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import MainCode.ClusteringAlgorithm.AbstractClustering;
import MainCode.ClusteringAlgorithm.KNearestNeighbor;
import MainCode.ClusteringAlgorithm.MeanShift;
import MainCode.InSetData.Node;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

import static MainCode.ClusteringAlgorithm.AbstractClustering.NORMAL_NODE_RADIUS;
import static MainCode.HelpfullFunction.withMathRound;

public class Controller implements Initializable {

    public static int MAX_K =10;
    public static int MAX_DIVERSITY =5;
    public static int MAX_OPTIMIXE_G =3;
    public static int NO_OF_NODES = 20;
    public static double SPEED = 500;

    @FXML private Label errorAndWarningLabel;
    @FXML private Label nameOfAlgorithmLabel;
    @FXML private Label speedDisplayLabel;
    @FXML private Label numPointLabel;
    @FXML private Label numOfNodes;
    @FXML private Label stepByStepStateLabel;
    @FXML private Label statusLabel;
    @FXML private Hyperlink hyperLinkGit;

    @FXML private Button addPointButton;
    @FXML private Button showCreditButton;
    @FXML private Button stepByStepOnButton;
    @FXML private Button stepByStepOffButton;
    @FXML private Button stepByStepNextButton;
    @FXML private Button testButton;
    @FXML private Button removePointButton;
    @FXML private Button forceStopButton;
    @FXML private Button stopPickByHandButton;
    @FXML private Button pickByHandButton;
    @FXML private Button resetButton;
    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button setTemplateFileButton;

    @FXML private ComboBox<Integer> numberPointButton;
    @FXML private ComboBox<AbstractClustering> chooseAlgorithmComboBox;
    @FXML private Slider speedSlider;

    @FXML private TextField optionForGenerateField;
    @FXML private TextField delayOfSpeedField;
    @FXML private TextField customKField;
    @FXML private TextField diversityForGenerateField;
    @FXML private TextField dragTemplateFileTextFiled;

    @FXML private AnchorPane show;//contain everything(becarefull when use)
    @FXML private Pane quitPane;
    @FXML private Pane display;
    @FXML private Pane loadingPicPane;
    @FXML private HBox quitHBox;
    @FXML private HBox dropDragTemplateFileTarget;
    @FXML private VBox credit;

    private int stepByStepState=0;
    private int currentTransition;
    private int optionForCenterPoint=1;
    private int dForGenerate=4;
    private int k=4;
    private double speedMul=1;
    private boolean running = false;
    private AbstractClustering abstractClustering;
    private String nameOfAlgorithm;
    private ArrayList<SequentialTransition> stArray;//tuan tu

    public void initialize(URL url, ResourceBundle resourceBundle) {
        numOfNodes.setText(String.valueOf(NO_OF_NODES));
        //initAllSparkDecoration();
        initQuitPaneAnimation();
        initSquareAnimationForDisplay();                                                                        //this is for the square show and hiding
        credit.setVisible(false);                                                                               //hide credit by default
        setLoadingPicPane();                                                                                    //loading pic when the algorithm run
        loadingPicPane.setVisible(false);                                                                       //loading pic hide by default
        ObservableList<Integer> numPointOption= FXCollections.observableArrayList(10,20,30);               //init the add and remove point random box
        numberPointButton.setValue(10);
        numberPointButton.setItems(numPointOption);
        List <AbstractClustering> sortListOption = new ArrayList<>();                                           //init the choose algorithm box
        sortListOption.add(new KMeans());
        sortListOption.add(new KNearestNeighbor());
        sortListOption.add(new MeanShift());
        chooseAlgorithmComboBox.setItems(FXCollections.observableArrayList(sortListOption));
        chooseAlgorithmComboBox.getSelectionModel().select(0);
        chooseAlgorithmComboBox.setConverter(new StringConverter<AbstractClustering>() {                        //display the simple name of algorithm
            @Override
            public String toString(AbstractClustering absSort) {
                if (absSort == null) return "";
                else return absSort.getClass().getSimpleName();
            }

            @Override
            public AbstractClustering fromString(String s) {
                return null;
            }
        });
        resetDisplayAndAlgorithm();                                                                             //show the nodes by default
        diversityForGenerateField.setText("4");                                                                 //init the value for the custom argument
        optionForGenerateField.setText("1");
        customKField.setText("4");
        delayOfSpeedField.setText("500");
        dropDragTemplateFileTarget.setOnDragOver(new EventHandler<DragEvent>() {                                //init the drag and drop file function
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != dropDragTemplateFileTarget
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        dropDragTemplateFileTarget.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    dragTemplateFileTextFiled.setText(db.getFiles().toString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }
    public void chooseAlgorithmComboBoxOnAction(ActionEvent event){
        resetDisplayAndAlgorithm();
        nameOfAlgorithmLabel.setText(chooseAlgorithmComboBox.getSelectionModel().getSelectedItem().getClass().getSimpleName());
    }
    public void resetButtonOnAction(ActionEvent event){
        resetDisplayAndAlgorithm();
    }
    public void resetDisplayAndAlgorithm(){
        currentTransition=0;
        checkCustomArgumentBeforeRunning();
        abstractClustering=chooseAlgorithmComboBox.getSelectionModel().getSelectedItem();//init the algorithm and the nodes too
        abstractClustering.initListOfNodes(NO_OF_NODES,dForGenerate,optionForCenterPoint);
        display.getChildren().remove(128,display.getChildren().size());
        display.getChildren().addAll(abstractClustering.getListOfNodes());
        try{
            stArray.clear();
        }catch (Exception e){ }
        running = false;
        setButtonStateBaseOnCondition();
    }
    public void forceStopButtonOnAction(ActionEvent event) {
        resetDisplayAndAlgorithm();
        errorAndWarningLabel.setText("Force Stop and reset Screen! May cause Animation Error!");
    }
    public void startButtonOnAction(ActionEvent event){
        speedSlider.setValue(1);
        speedDisplayLabel.setText(String.valueOf(1));
        loadingPicPane.setVisible(true);
        checkCustomArgumentBeforeRunning();
        running=true;
        statusLabel.setText("Running");
        setButtonStateBaseOnCondition();         // auto init centroids
        stArray=new ArrayList<>();
        stArray.addAll(Arrays.asList(abstractClustering.startSort(this.k,display)));
        stArray.get(0).play();
        if(stepByStepState==0){
            for(int i=1;i<stArray.size();i++){
                int finalI = i;
                stArray.get(i-1).setOnFinished(e->{
                    try{
                        stArray.get(finalI).play();
                        currentTransition++;
                    }catch (Exception ex){
                        errorAndWarningLabel.setText("Force Stop!");
                    }
                });
            }
        }else{
            currentTransition++;
        }
        stArray.get(stArray.size()-1).setOnFinished(e -> {
            running=false;
            setButtonStateBaseOnCondition();
            setButtonStateAfterRunning();
            soundAfterDone();
        });
    }
    public void soundAfterDone(){
        String bip = "C:\\Users\\HAU.DV184093\\Favorites\\Code\\Java\\SorttingApp\\Self\\Ver5_Building\\src\\MainCode\\done1.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
    public void setButtonStateAfterRunning(){
        startButton.setDisable(true);
        addPointButton.setDisable(true);
        removePointButton.setDisable(true);
        numberPointButton.setDisable(true);
        statusLabel.setText("Finished");
    }
    public void pickByHandButtonOnAction(ActionEvent event){
        display.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                String msg =
                        "x= "       + Math.round(event.getX() )     + ", y= "       +  Math.round(event.getY())+"!" ;
                NO_OF_NODES+=1;
                Node nodeTemp=new Node();
                nodeTemp.setCenterX(event.getX());
                nodeTemp.setCenterY(event.getY());
                nodeTemp.setRadius(NORMAL_NODE_RADIUS);
                nodeTemp.setFill(Color.BLACK);

                abstractClustering.addANodeToListOfNodes(nodeTemp);
                display.getChildren().add(nodeTemp);
                numOfNodes.setText(String.valueOf(NO_OF_NODES));
                errorAndWarningLabel.setText("Added a node at " + msg);
                setButtonStateBaseOnCondition();
            }
        });
        display.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                errorAndWarningLabel.setText("Please choose a point within display pane!");
            }
        });
    }
    public void stopPickByHandButtonOnAction(ActionEvent event){
        display.setOnMouseClicked(null);
        display.setOnMouseExited(null);
        errorAndWarningLabel.setText("Stop Pick by hand mode!");
    }
    public void pauseButtonOnAction(){
        if(stArray.get(currentTransition).getStatus()==Animation.Status.RUNNING){
            stArray.get(currentTransition).pause();
            loadingParraTrans.pause();
            pauseButton.setText("Resume");
            pauseButton.setStyle("-fx-background-color:#2ecc71");
            statusLabel.setText("Paused");
        }else{
            stArray.get(currentTransition).play();
            loadingParraTrans.play();
            pauseButton.setText("Pause");
            pauseButton.setStyle("-fx-background-color: #ff7979");
            statusLabel.setText("Running");
        }
    }
    public void speedSliderOnChange(){
        speedMul=withMathRound(speedSlider.getValue(),2);
        if(speedMul==1) speedDisplayLabel.setTextFill(Color.BLACK);
        else if(speedMul>1&& speedMul<3) speedDisplayLabel.setTextFill(Color.web("#2ecc71"));
        else if(speedMul>=3&& speedMul<5) speedDisplayLabel.setTextFill(Color.web("#f39c12"));
        else speedDisplayLabel.setTextFill(Color.web("#c0392b"));
        speedDisplayLabel.setText(String.valueOf(speedMul));
        try {
            for (int i = 0; i < stArray.size(); i++) {
                stArray.get(i).rateProperty().setValue(speedMul);
            }
        }catch (Exception e){ }
    }
    public void delayOfSpeedFieldOnEnter(KeyEvent ke){
        if(ke.getCode()==KeyCode.ENTER) {
            try {
                double oldValueOfSPEED = this.SPEED;
                SPEED = Integer.parseInt(delayOfSpeedField.getText());
                if (SPEED < 0) {
                    SPEED = oldValueOfSPEED;
                    errorAndWarningLabel.setText("DELAY must be greater than 0. Auto rollback!");
                }
            } catch (Exception e) {
                errorAndWarningLabel.setText("Wrong input in DELAY .Auto rollback!");
            }
            delayOfSpeedField.setText(String.valueOf(SPEED));
        }
    }
    public void dForGenerateFieldOnEnter(KeyEvent ke){
        if(ke.getCode()==KeyCode.ENTER) {
            try {
                int oldValueOfdForGenerate=this.dForGenerate;
                this.dForGenerate = Integer.parseInt(diversityForGenerateField.getText());
                if(this.dForGenerate>MAX_DIVERSITY || this.dForGenerate<1){
                    this.dForGenerate=oldValueOfdForGenerate;
                    errorAndWarningLabel.setText("Diversity must be from 1-5.Auto rollback!");
                }
            }catch (Exception e){
                errorAndWarningLabel.setText("Wrong input in Diversity. Auto rollback!");
            }
            diversityForGenerateField.setText(String.valueOf(this.dForGenerate));
        }
    }
    public void optimizeGFieldOnEnter(KeyEvent ke){
        if(ke.getCode()==KeyCode.ENTER) {
            try {
                int oldValueOfoptionForCenterPoint=this.optionForCenterPoint;
                this.optionForCenterPoint = Integer.parseInt(optionForGenerateField.getText());
                if(this.optionForCenterPoint>MAX_OPTIMIXE_G || this.optionForCenterPoint<1){
                    this.optionForCenterPoint=oldValueOfoptionForCenterPoint;
                    errorAndWarningLabel.setText("OptimizeG must be from 1-3.Auto rollback!");
                }
            }catch (Exception e){
                errorAndWarningLabel.setText("Wrong input in OptimizeG. Auto rollback!");
            }
            optionForGenerateField.setText(String.valueOf(this.optionForCenterPoint));
        }
    }
    public void kFieldOnEnter(KeyEvent ke){
        if(ke.getCode()==KeyCode.ENTER) {
            try {
                int oldValueOfk=this.k;
                this.k=Integer.parseInt(customKField.getText());
                if(this.k>MAX_K || this.dForGenerate<1){
                    this.k=oldValueOfk;

                    errorAndWarningLabel.setText("k must be from 1-10.Auto rollback!");
                }
            }catch (Exception e){
                errorAndWarningLabel.setText("Wrong input in OptimizeG. Auto rollback!");
            }
            customKField.setText(String.valueOf(this.k));
        }
    }
    public void checkCustomArgumentBeforeRunning(){
        errorAndWarningLabel.setText("");
        delayOfSpeedField.setText(String.valueOf(SPEED));
        diversityForGenerateField.setText(String.valueOf(this.dForGenerate));
        optionForGenerateField.setText(String.valueOf(this.optionForCenterPoint));
        customKField.setText(String.valueOf(this.k));
    }
    public void stepByStepOnButtonOnAction(ActionEvent event){
        stepByStepState=1;
        stepByStepOnButton.setDisable(true);
        stepByStepOffButton.setDisable(false);
        stepByStepStateLabel.setText("On");
    }
    public void stepByStepOffButtonOnAction(ActionEvent event){
        stepByStepState=0;
        stepByStepOnButton.setDisable(false);
        stepByStepOffButton.setDisable(true);
        stepByStepStateLabel.setText("Off");
    }
    public void stepBystepNextButtonOnAction(ActionEvent event){
        stepByStepNextButton.setDisable(true);
        stArray.get(currentTransition).play();
        stArray.get(currentTransition).setOnFinished(e -> {
            stepByStepNextButton.setDisable(false);
        });
        if(currentTransition==stArray.size()-1) {
            stepByStepNextButton.setDisable(true);
            running=false;
            setButtonStateBaseOnCondition();
        }
        currentTransition++;
    }
    public void setButtonStateBaseOnCondition(){
        if(running){
            resetButton.setDisable(true);
            stepByStepOffButton.setDisable(true);
            stepByStepOnButton.setDisable(true);
            if(stepByStepState==0) {
                stepByStepNextButton.setDisable(true);
            }else{
                stepByStepNextButton.setDisable(false);
            }
            optionForGenerateField.setDisable(true);
            delayOfSpeedField.setDisable(true);
            customKField.setDisable(true);
            diversityForGenerateField.setDisable(true);
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            removePointButton.setDisable(true);
            addPointButton.setDisable(true);
            chooseAlgorithmComboBox.setDisable(true);
            numberPointButton.setDisable(true);
            pickByHandButton.setDisable(true);
            stopPickByHandButton.setDisable(true);
            loadingPicPane.setVisible(true);
            setTemplateFileButton.setDisable(true);
        }else{
            resetButton.setDisable(false);
            if(stepByStepState==0) {
                stepByStepNextButton.setDisable(true);
                stepByStepOffButton.setDisable(true);
                stepByStepOnButton.setDisable(false);
            }else{
                stepByStepNextButton.setDisable(true);
                stepByStepOffButton.setDisable(false);
                stepByStepOnButton.setDisable(true);
            }
            optionForGenerateField.setDisable(false);
            delayOfSpeedField.setDisable(false);
            customKField.setDisable(false);
            diversityForGenerateField.setDisable(false);
            pauseButton.setDisable(true);
            if(NO_OF_NODES-numberPointButton.getSelectionModel().getSelectedItem()>=10) removePointButton.setDisable(false);
            else removePointButton.setDisable(true);
            addPointButton.setDisable(false);
            chooseAlgorithmComboBox.setDisable(false);
            numberPointButton.setDisable(false);
            startButton.setDisable(false);
            pickByHandButton.setDisable(false);
            stopPickByHandButton.setDisable(false);
            loadingPicPane.setVisible(false);
            setTemplateFileButton.setDisable(false);
        }
    }
    public void selectNumPoint(ActionEvent event) {
        String s=numberPointButton.getSelectionModel().getSelectedItem().toString();
        numPointLabel.setText("Add/Remove:"+s);
        setButtonStateBaseOnCondition();
    }
    public void addPointButtonOnAction(ActionEvent event){
        int addNumber=numberPointButton.getSelectionModel().getSelectedItem();
        NO_OF_NODES+=addNumber;
        Node[] nodeTemp=abstractClustering.addSomeNodeToListOfNode(addNumber,this.dForGenerate,this.optionForCenterPoint);
        display.getChildren().addAll(Arrays.asList(nodeTemp));
        numOfNodes.setText(String.valueOf(NO_OF_NODES));
        setButtonStateBaseOnCondition();
    }
    public void removePointButtonOnAction(ActionEvent event){
        int removeNumber=numberPointButton.getSelectionModel().getSelectedItem();
        abstractClustering.removeSomeNodeInListOfNode(removeNumber);
        display.getChildren().remove(display.getChildren().size()-removeNumber,display.getChildren().size());
        NO_OF_NODES-=removeNumber;
        numOfNodes.setText(String.valueOf(NO_OF_NODES));
        setButtonStateBaseOnCondition();
    }
    public void setTemplateFileButtonOnAction(ActionEvent event){
        currentTransition=0;
        display.getChildren().remove(128,display.getChildren().size());
        try{
            stArray.clear();
        }catch (Exception e){
        };
        running = false;
        Node[] nodeTemp=HelpfullFunction.getNodesFromFile(dragTemplateFileTextFiled.getText());
        abstractClustering.overrideListOfNodes(nodeTemp);
        display.getChildren().addAll(nodeTemp);
        NO_OF_NODES=nodeTemp.length;
        numOfNodes.setText(String.valueOf(NO_OF_NODES));
        setButtonStateBaseOnCondition();
    }




    @FXML private Pane sparkDecoration1;
    @FXML private Pane sparkDecoration2;
    @FXML private Pane sparkDecoration3;
    @FXML private Pane sparkDecoration4;
    private void initAllSparkDecoration(){
        initSparkDecorationType1(sparkDecoration1,1.5,1780,3780);
        initSparkDecorationType1(sparkDecoration2,1.5,2578,3000);
        initSparkDecorationType1(sparkDecoration3,1.5,3560,1250);
        initSparkDecorationType1(sparkDecoration4,3,5000,3645);
    }
    private  void initSparkDecorationType1(Pane somePane, double radius,double outTime,double inTime){
        Circle cir1=new Circle();
        cir1.setCenterX(20);
        cir1.setCenterY(20);
        cir1.setFill(Color.web("#ffffff",0));
        cir1.setRadius(radius);
        somePane.getChildren().add(cir1);
        SequentialTransition zoomInAndOut=new SequentialTransition();
        //zoom out
        ParallelTransition zoomOut=new ParallelTransition();
        FillTransition fillOut=new FillTransition();
        fillOut.setShape(cir1);
        fillOut.setToValue(Color.web("#ffffff",0.8));
        fillOut.setDuration(Duration.millis(outTime));
        ScaleTransition scaleOut = new ScaleTransition();
        scaleOut.setNode(cir1);
        scaleOut.setDuration(Duration.millis(outTime));
        scaleOut.setToX(1);
        scaleOut.setToY(1);
        zoomOut.getChildren().addAll(fillOut,scaleOut);
        //zoom in
        ParallelTransition zoomIn=new ParallelTransition();
        FillTransition fillIn=new FillTransition();
        fillIn.setShape(cir1);
        fillIn.setToValue(Color.web("#ffffff",0));
        fillIn.setDuration(Duration.millis(inTime));
        ScaleTransition scaleIn = new ScaleTransition();
        scaleIn.setNode(cir1);
        scaleIn.setDuration(Duration.millis(inTime));
        scaleIn.setToX(0.8);
        scaleIn.setToY(0.8);
        zoomIn.getChildren().addAll(fillIn,scaleIn);
        //rest
        FillTransition rest=new FillTransition();
        rest.setShape(cir1);
        rest.setToValue(Color.web("#ffffff",0));
        rest.setDuration(Duration.millis(inTime));

        zoomInAndOut.getChildren().addAll(zoomOut,zoomIn,rest);
        zoomInAndOut.setCycleCount(Animation.INDEFINITE);
        zoomInAndOut.play();
    }

    private int quitHBoxState=0;
    public void initQuitPaneAnimation(){
        quitPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if(quitHBoxState==0) {
                    TranslateTransition quitHBoxApear=new TranslateTransition();
                    quitHBoxApear.setNode(quitHBox);
                    quitHBoxApear.setByX(42);
                    quitHBoxApear.setDuration(Duration.millis(500));
                    quitHBoxApear.play();
                    quitHBoxApear.setOnFinished(e -> {
                        quitHBoxState=1;
                        quitHBox.setTranslateX(42);
                    });
                }
            }
        });
        quitPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (quitHBoxState==1) {
                    TranslateTransition quitHBoxDisapear = new TranslateTransition();
                    quitHBoxDisapear.setNode(quitHBox);
                    quitHBoxDisapear.setByX(-42);
                    quitHBoxDisapear.setDuration(Duration.millis(500));
                    quitHBoxDisapear.play();
                    quitHBoxDisapear.setOnFinished(e -> {
                        quitHBoxState=0;
                        quitHBox.setTranslateX(0);
                    });
                }
            }
        });
    }
    public void quitButtonOnAction (ActionEvent event){
//        Color recColor=Color.web("#27ae60",1);
//        Color invColor=Color.web("#27ae60",0);
//        double timeUntilExit=200;
//        show.getChildren().removeAll(show.getChildren());
//        Label textPromt=new Label("Terminating");
//        textPromt.setTextFill(recColor);
//        textPromt.setLayoutX(530);
//        textPromt.setLayoutY(350);
//        textPromt.setFont(new Font("Cambria", 80));
//
//        Circle cir1=new Circle(970,420,8,invColor);
//        Circle cir2=new Circle(1010,420,8,invColor);
//        Circle cir3=new Circle(1050,420,8,invColor);
//        show.getChildren().addAll(textPromt,cir1,cir2,cir3);
//        FillTransition fill1=new FillTransition();
//        fill1.setShape(cir1);
//        fill1.setToValue(recColor);
//        fill1.setDuration(Duration.millis(timeUntilExit));
//        FillTransition fill2=new FillTransition();
//        fill2.setShape(cir2);
//        fill2.setToValue(recColor);
//        fill2.setDuration(Duration.millis(timeUntilExit));
//        FillTransition fill3=new FillTransition();
//        fill3.setShape(cir3);
//        fill3.setToValue(recColor);
//        fill3.setDuration(Duration.millis(timeUntilExit));
//        SequentialTransition circleShow=new SequentialTransition();
//        circleShow.getChildren().addAll(fill1,fill2,fill3);
//        circleShow.play();
//        circleShow.setOnFinished(e->{
            System.exit(0);
//        });
    }
    private ParallelTransition loadingParraTrans=new ParallelTransition();
    public void setLoadingPicPane(){
        double LOADING_START_Y=20;
        double LOADING_GOAL_Y=30;
        int LOADING_DELAY=500;
        int LOADING_CIRCLE_RADIUS=5;
        Circle cir1 = new Circle(45,LOADING_START_Y,LOADING_CIRCLE_RADIUS);
        Circle cir2 = new Circle(65,LOADING_START_Y,LOADING_CIRCLE_RADIUS);
        Circle cir3 = new Circle(85,LOADING_START_Y,LOADING_CIRCLE_RADIUS);
        Circle cir4 = new Circle(105,LOADING_START_Y,LOADING_CIRCLE_RADIUS);

        //setting colours and strokes for circle
        cir1.setFill(Color.BLUE);
        cir2.setFill(Color.LIMEGREEN);
        cir3.setFill(Color.RED);
        cir4.setFill(Color.PURPLE);

        TranslateTransition transAdd1=new TranslateTransition();
        transAdd1.setDuration(Duration.millis(0));
        TranslateTransition trans1 = new TranslateTransition();
        trans1.setByY(LOADING_GOAL_Y);
        trans1.setCycleCount(2);
        trans1.setDuration(Duration.millis(LOADING_DELAY));
        trans1.setAutoReverse(true);
        trans1.setNode(cir1);
        SequentialTransition transSum1=new SequentialTransition();
        transSum1.getChildren().addAll(transAdd1,trans1);

        TranslateTransition transAdd2=new TranslateTransition();
        transAdd2.setDuration(Duration.millis(100));
        TranslateTransition trans2 = new TranslateTransition();
        trans2.setByY(LOADING_GOAL_Y);
        trans2.setCycleCount(2);
        trans2.setDuration(Duration.millis(LOADING_DELAY));
        trans2.setAutoReverse(true);
        trans2.setNode(cir2);
        SequentialTransition transSum2=new SequentialTransition();
        transSum2.getChildren().addAll(transAdd2,trans2);

        TranslateTransition transAdd3=new TranslateTransition();
        transAdd3.setDuration(Duration.millis(200));
        TranslateTransition trans3 = new TranslateTransition();
        trans3.setByY(LOADING_GOAL_Y);
        trans3.setCycleCount(2);
        trans3.setDuration(Duration.millis(LOADING_DELAY));
        trans3.setAutoReverse(true);
        trans3.setNode(cir3);
        SequentialTransition transSum3=new SequentialTransition();
        transSum3.getChildren().addAll(transAdd3,trans3);

        TranslateTransition transAdd4=new TranslateTransition();
        transAdd4.setDuration(Duration.millis(300));
        TranslateTransition trans4 = new TranslateTransition();
        trans4.setByY(LOADING_GOAL_Y);
        trans4.setCycleCount(2);
        trans4.setDuration(Duration.millis(LOADING_DELAY));
        trans4.setAutoReverse(true);
        trans4.setNode(cir4);
        SequentialTransition transSum4=new SequentialTransition();
        transSum4.getChildren().addAll(transAdd4,trans4);

        loadingParraTrans.getChildren().addAll(transSum1,transSum2, transSum3, transSum4);
        loadingParraTrans.setCycleCount(Animation.INDEFINITE);
        loadingParraTrans.play();
        loadingPicPane.getChildren().addAll(cir1,cir2,cir3,cir4);
    }
    public void hyperLinkGitOnAction(ActionEvent event) {
        String url="https://github.com/DoanVanHau20184093";
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException ignored) { }
    }
    private int creditShow=0;
    public void showCreditButtonOnAction(ActionEvent event){
        if(creditShow==1){
            credit.setVisible(false);
            creditShow=0;
        }else{
            creditAnimation();
            creditShow=1;
        }
    }
    public void initSquareAnimationForDisplay(){//init the animation for the display, show and hide the squares
        int verSquareNum=16;
        int horSquareNum=8;
        Color squareFillColor=Color.web("#40739e",0.04);
        Color squareStrokeColor=Color.web("#ffffff",0.5);
        double opacityShow=0.2;
        Rectangle[] squareForDisplay=new Rectangle[verSquareNum*horSquareNum];
        double distance=50;
        int currentSquare=0;
        for(int i=0;i<verSquareNum;i++){
            for(int j=0;j<horSquareNum;j++){
                squareForDisplay[currentSquare]=new Rectangle();
                squareForDisplay[currentSquare].setX(5+i*distance);
                squareForDisplay[currentSquare].setY(5+j*distance);
                squareForDisplay[currentSquare].setHeight(40);
                squareForDisplay[currentSquare].setWidth(40);
                squareForDisplay[currentSquare].setFill(Color.web("#40739e",0));
                squareForDisplay[currentSquare].setStroke(Color.web("#ffffff",0));
                currentSquare++;
            }
        }
        display.getChildren().addAll(squareForDisplay);
        ////////////////////////////////////////////////////////////////////////////////////////show and hide square
        ParallelTransition allSquaresShowAndHide=new ParallelTransition();
        for(int i=0;i<verSquareNum*horSquareNum;i++) {
            SequentialTransition eachSquareShowAndHide=new SequentialTransition();
            ////////////show square
            ParallelTransition showSquare=new ParallelTransition();
            double showTime=HelpfullFunction.getRandomDoubleInRange(1000,6000);
            StrokeTransition showStrokeTrans=new StrokeTransition();
            showStrokeTrans.setShape(squareForDisplay[i]);
            showStrokeTrans.setToValue(squareStrokeColor);
            showStrokeTrans.setDuration(Duration.millis(showTime));

            FillTransition showFillTrans =new FillTransition();
            showFillTrans.setShape(squareForDisplay[i]);
            showFillTrans.setToValue(squareFillColor);
            showFillTrans.setDuration(Duration.millis(showTime));
            showSquare.getChildren().addAll(showStrokeTrans,showFillTrans);
            //////////hide square
            ParallelTransition hideSquare=new ParallelTransition();
            double hideTime=HelpfullFunction.getRandomDoubleInRange(1000,2000);
            StrokeTransition hideStrokeTrans=new StrokeTransition();
            hideStrokeTrans.setShape(squareForDisplay[i]);
            hideStrokeTrans.setToValue(Color.web("#ffffff",0));
            hideStrokeTrans.setDuration(Duration.millis(hideTime));

            FillTransition hideFillTrans =new FillTransition();
            hideFillTrans.setShape(squareForDisplay[i]);
            hideFillTrans.setToValue(Color.web("#40739e",0));
            hideFillTrans.setDuration(Duration.millis(hideTime));
            hideSquare.getChildren().addAll(hideStrokeTrans,hideFillTrans);

            eachSquareShowAndHide.getChildren().addAll(showSquare,hideSquare);
            allSquaresShowAndHide.getChildren().add(eachSquareShowAndHide);
        }
        allSquaresShowAndHide.setCycleCount(Animation.INDEFINITE);
        allSquaresShowAndHide.play();
    }
    public void creditAnimation(){
        showCreditButton.setDisable(true);
        Color flyInRecColorHide=Color.web("#dfe6e9",0);
        Color flyInRecColorShow=Color.web("#dfe6e9",1);
        double DelayFlyIn=500;
        double fillDelay=650;
        Rectangle flyInRec1=new Rectangle();
        flyInRec1.setX(1027);
        flyInRec1.setY(10-1000);//from top
        flyInRec1.setFill(flyInRecColorHide);
        flyInRec1.setWidth(230.5);
        flyInRec1.setHeight(381);
        Rectangle flyInRec2=new Rectangle();
        flyInRec2.setX(1257.5+1000);//from right
        flyInRec2.setY(10);
        flyInRec2.setFill(flyInRecColorHide);
        flyInRec2.setWidth(230.5);
        flyInRec2.setHeight(381);
        Rectangle flyInRec3=new Rectangle();
        flyInRec3.setX(1027);
        flyInRec3.setY(389+1000);//from bottom
        flyInRec3.setFill(flyInRecColorHide);
        flyInRec3.setWidth(230.5);
        flyInRec3.setHeight(381);
        Rectangle flyInRec4=new Rectangle();
        flyInRec4.setX(1257.5+1000);//from right+bottom
        flyInRec4.setY(389+1000);
        flyInRec4.setFill(flyInRecColorHide);
        flyInRec4.setWidth(230.5);
        flyInRec4.setHeight(381);

        ParallelTransition allRecFlyIn=new ParallelTransition();
        //Rec1
        ParallelTransition rec1FlyIn=new ParallelTransition();
        TranslateTransition rec1Translate=new TranslateTransition();
        rec1Translate.setNode(flyInRec1);
        rec1Translate.setByY(1000);
        rec1Translate.setDuration(Duration.millis(DelayFlyIn));
        FillTransition rec1FillColor=new FillTransition();
        rec1FillColor.setShape(flyInRec1);
        rec1FillColor.setToValue(flyInRecColorShow);
        rec1FillColor.setDuration(Duration.millis(fillDelay));
        rec1FlyIn.getChildren().addAll(rec1Translate,rec1FillColor);
        //rec2
        ParallelTransition rec2FlyIn=new ParallelTransition();
        TranslateTransition rec2Translate=new TranslateTransition();
        rec2Translate.setNode(flyInRec2);
        rec2Translate.setByX(-1000);
        rec2Translate.setDuration(Duration.millis(DelayFlyIn));
        FillTransition rec2FillColor=new FillTransition();
        rec2FillColor.setShape(flyInRec2);
        rec2FillColor.setToValue(flyInRecColorShow);
        rec2FillColor.setDuration(Duration.millis(fillDelay));
        rec2FlyIn.getChildren().addAll(rec2Translate,rec2FillColor);
        ///rec 3
        ParallelTransition rec3FlyIn=new ParallelTransition();
        TranslateTransition rec3Translate=new TranslateTransition();
        rec3Translate.setNode(flyInRec3);
        rec3Translate.setByY(-1000);
        rec3Translate.setDuration(Duration.millis(DelayFlyIn));
        FillTransition rec3FillColor=new FillTransition();
        rec3FillColor.setShape(flyInRec3);
        rec3FillColor.setToValue(flyInRecColorShow);
        rec3FillColor.setDuration(Duration.millis(fillDelay));
        rec3FlyIn.getChildren().addAll(rec3Translate,rec3FillColor);
        ///rec 4
        ParallelTransition rec4FlyIn=new ParallelTransition();
        TranslateTransition rec4Translate=new TranslateTransition();
        rec4Translate.setNode(flyInRec4);
        rec4Translate.setByX(-1000);
        rec4Translate.setByY(-1000);
        rec4Translate.setDuration(Duration.millis(DelayFlyIn));
        FillTransition rec4FillColor=new FillTransition();
        rec4FillColor.setShape(flyInRec4);
        rec4FillColor.setToValue(flyInRecColorShow);
        rec4FillColor.setDuration(Duration.millis(fillDelay));
        rec4FlyIn.getChildren().addAll(rec4Translate,rec4FillColor);

        show.getChildren().addAll(flyInRec1,flyInRec2,flyInRec3,flyInRec4);
        allRecFlyIn.getChildren().addAll(rec1FlyIn,rec2FlyIn,rec3FlyIn,rec4FlyIn);
        allRecFlyIn.play();
        allRecFlyIn.setOnFinished((e->{
            show.getChildren().remove(flyInRec1);
            show.getChildren().remove(flyInRec2);
            show.getChildren().remove(flyInRec3);
            show.getChildren().remove(flyInRec4);
            credit.setVisible(true);
            showCreditButton.setDisable(false);

        }));
    }
    public void testButtonOnAction(ActionEvent event){
    }
    public void initGridAnimationForDisplay(){
        ParallelTransition linesShowAndHide=new ParallelTransition();
        int verLineNum=80;
        int horLineNum=40;
        Line[] verticleLines=new Line[verLineNum-1];
        Line[] horizontalLines=new Line[horLineNum-1];
        int percentChooseLines=3;
        String verColor="#2ecc71";
        String horColor="#e74c3c";
        double opacityShow=0.2;
        double countX=10;
        for(int i=0;i<verLineNum-1;i++){
            verticleLines[i]=new Line();
            verticleLines[i].setStartX(countX);
            verticleLines[i].setEndX(countX);
            verticleLines[i].setStartY(0);
            verticleLines[i].setEndY(400);
            verticleLines[i].setStroke(Color.web("#2ecc71",0));
            countX+=800/verLineNum;
        }display.getChildren().addAll(verticleLines);
        int countY=10;
        for(int i=0;i<horLineNum-1;i++){
            horizontalLines[i]=new Line();
            horizontalLines[i].setStartX(0);
            horizontalLines[i].setEndX(800);
            horizontalLines[i].setStartY(countY);
            horizontalLines[i].setEndY(countY);
            horizontalLines[i].setStroke(Color.web("#2ecc71",0));
            countY+=400/horLineNum;
        }display.getChildren().addAll(horizontalLines);
        ////////////////////////////////////////////////////////////////////////////////////////vertical show and hide
        SequentialTransition verLinesShowAndHide=new SequentialTransition();
        ParallelTransition verLinesShow=new ParallelTransition();
        ParallelTransition verLinesHide=new ParallelTransition();
        for(int i=0;i<verLineNum-1;i++) {
            StrokeTransition verStrokeTrans=new StrokeTransition();
            verStrokeTrans.setShape(verticleLines[i]);
            verStrokeTrans.setToValue(Color.web(verColor,opacityShow));
            verStrokeTrans.setDuration(Duration.millis(HelpfullFunction.getRandomDoubleInRange(400,700)));
            verLinesShow.getChildren().add(verStrokeTrans);
        }//show verticale lines
        for(int i=0;i<verLineNum-1;i++) {
            StrokeTransition verStrokeTrans=new StrokeTransition();
            verStrokeTrans.setShape(verticleLines[i]);
            verStrokeTrans.setToValue(Color.web(verColor,0));
            verStrokeTrans.setDuration(Duration.millis(HelpfullFunction.getRandomDoubleInRange(2000,2500)));
            verLinesHide.getChildren().add(verStrokeTrans);
        }//hide verticale lines
        verLinesShowAndHide.getChildren().addAll(verLinesShow,verLinesHide);
        ////////////////////////////////////////////////////////////////////////////////////////////horizontal show and hide
        SequentialTransition horLinesShowAndHide=new SequentialTransition();
        ParallelTransition horLinesShow=new ParallelTransition();
        ParallelTransition horLinesHide=new ParallelTransition();
        for(int i=0;i<horLineNum-1;i++) {
            StrokeTransition horStrokeTrans=new StrokeTransition();
            horStrokeTrans.setShape(horizontalLines[i]);
            horStrokeTrans.setToValue(Color.web(horColor,opacityShow));
            horStrokeTrans.setDuration(Duration.millis(HelpfullFunction.getRandomDoubleInRange(700,1000)));
            horLinesShow.getChildren().add(horStrokeTrans);
        }//show horizontal lines
        for(int i=0;i<horLineNum-1;i++) {
            StrokeTransition horStrokeTrans=new StrokeTransition();
            horStrokeTrans.setShape(horizontalLines[i]);
            horStrokeTrans.setToValue(Color.web(horColor,0));
            horStrokeTrans.setDuration(Duration.millis(HelpfullFunction.getRandomDoubleInRange(1500,2000)));
            horLinesHide.getChildren().add(horStrokeTrans);
        }//hide horizontal lines
        horLinesShowAndHide.getChildren().addAll(horLinesShow,horLinesHide);

        linesShowAndHide.getChildren().add(verLinesShowAndHide);
        linesShowAndHide.getChildren().add(horLinesShowAndHide);
        linesShowAndHide.setCycleCount(Animation.INDEFINITE);
        linesShowAndHide.play();
    }
}
