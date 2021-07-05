package MainCode;

import MainCode.InSetData.Node;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static MainCode.ClusteringAlgorithm.AbstractClustering.NORMAL_NODE_RADIUS;

public class HelpfullFunction {
    public static double withMathRound(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    public static int[] getRandomArray(int sumOfElements, int numberOfElement){
        java.util.Random g = new java.util.Random();
        int vals[] = new int[numberOfElement];
        if(sumOfElements==1) {vals[0]=1;return vals;}
        sumOfElements -= numberOfElement;
        for (int i = 0; i < numberOfElement-1; ++i) {
            vals[i] = g.nextInt(sumOfElements);
        }
        vals[numberOfElement-1] = sumOfElements;
        java.util.Arrays.sort(vals);
        for (int i = numberOfElement-1; i > 0; --i) {
            vals[i] -= vals[i-1];
        }
        for (int i = 0; i < numberOfElement; ++i) { ++vals[i]; }
        return vals;
    }
    public static double getRandomDoubleInRange(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }
    public static int getRandomIntInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
//    public static String removeStartAndEndOfAString(String str) {
//        String newString=
//    }
    public static Node[] getNodesFromFile(String rawString){
        File templateFile ;
        if (rawString.contains("[")){
            String fileLocation=rawString.replace("[","").replace("]","");
            templateFile = new File(fileLocation);
        }else{
            templateFile = new File(rawString);
        }


        Scanner myReader = null;
        try {
            myReader = new Scanner(templateFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int fileType = myReader.nextInt();
        int numOfNodes = myReader.nextInt();
        myReader.useDelimiter(",");
        Node[] nodesTemp= new Node[numOfNodes];
        for(int i=0;i<numOfNodes;i++){
            String centerXString=myReader.next().strip();
            String centerYString=myReader.next().strip();
            String color = myReader.next().strip();
            int centerX=Integer.parseInt(centerXString);
            int centerY=Integer.parseInt(centerYString);
            nodesTemp[i]=new Node();
            nodesTemp[i].setCenterX(centerX);
            nodesTemp[i].setCenterY(centerY);
            nodesTemp[i].setFill(Color.web(color));
            nodesTemp[i].setRadius(NORMAL_NODE_RADIUS);
        }
        myReader.close();
        return nodesTemp;
    }
    public static Integer[] createARandomArrayWithDistinctElementFrom0ToX(int x){
        Integer[] initArray=new Integer[x];
        for(int i=0;i<x;i++){
            initArray[i]=i;
        }
        List<Integer> intList = Arrays.asList(initArray);
        Collections.shuffle(intList);
        intList.toArray(initArray);
        return initArray;
    }
}
