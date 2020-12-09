package test;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.decimal4j.util.DoubleRounder;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements Initializable {

    String testData;
    boolean allSelected;
    @FXML
    HBox mainBox;
    @FXML
    CheckBox bitBox, seriesBox, longSBox, pokerBox;
    @FXML
    Button selectButton, loadButton, startButton, saveButton;
    @FXML
    FlowPane resultsPane;
    @FXML
    Label singleBitLabel, seriesLabel, longSLabel, pokerLabel;
    @FXML
    Label singleBitsResult, series1, series2, series3, series4, series5, series6, longSeriesResult, pokerResult;


    Label[] series;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        startButton.setDisable(true);
        allSelected = false;
        resultsPane.setVisible(false);
        saveButton.setDisable(true);
        series = new Label[] {series1, series2, series3, series4, series5, series6};
    }

    @FXML
    private void onSelectAll(ActionEvent actionEvent) {
        if(!allSelected){
            bitBox.setSelected(true);
            seriesBox.setSelected(true);
            longSBox.setSelected(true);
            pokerBox.setSelected(true);
            allSelected = true;
            selectButton.setText("Odznacz wszystkie");
        }
        else{
            bitBox.setSelected(false);
            seriesBox.setSelected(false);
            longSBox.setSelected(false);
            pokerBox.setSelected(false);
            allSelected = false;
            selectButton.setText("Zaznacz wszystkie");
        }
    }
    @FXML
    private void onLoad(ActionEvent actionEvent){
        testData = FileHandler.openFromFile();
        try {
            if (testData.length() == 20000) {
                startButton.setDisable(false);
            } else if (testData.length() > 20000) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Nieprawidłowa długość danych." +
                        " Czy wziąć tylko pierwsze 20 tysięcy znaków?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Informacja");
                alert.setHeaderText("");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    testData = testData.substring(0, 20000);
                    startButton.setDisable(false);
                } else {
                    testData = "";
                    return;
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Dostarczone dane są za krótkie!",
                        ButtonType.OK);
                alert.setTitle("Błąd");
                alert.setHeaderText("");
                alert.showAndWait();
            }
        }
        catch(NullPointerException e){

        }
    }

    @FXML
    private void onStart(ActionEvent actionEvent) {
        resultsPane.setVisible(true);
        clearResults();
        if(bitBox.isSelected()){
            testBits();
        }
        if(seriesBox.isSelected()) {
            testSeries();
        }
        if(longSBox.isSelected()){
            testLongSeries();
        }
        if(pokerBox.isSelected()){
            testPoker();
        }
        saveButton.setDisable(false);
    }

    private void clearResults() {
        singleBitLabel.setText("");
        singleBitsResult.setText("");
        seriesLabel.setText("");
        Arrays.stream(series).forEach(s -> {
            s.setText("");
        });

    }

    class Pair {
        int min;
        int max;
        public Pair(int min, int max) {
            this.min = min;
            this.max = max;
        }

    }

    private void testBits(){
        int singleBits = Tests.monobitTest(testData);
        if(singleBits > 9725 && singleBits < 10275){
            singleBitLabel.setTextFill(Color.DARKGREEN);
            singleBitLabel.setText("Zaliczone");
            singleBitsResult.setTextFill(Color.BLACK);
        }
        else {
            singleBitLabel.setTextFill(Color.DARKRED);
            singleBitLabel.setText("Niepowodzenie");
            singleBitsResult.setTextFill(Color.DARKRED);
        }
        singleBitsResult.setText(Integer.toString(singleBits));
    }
    private void testSeries(){
        Map<Integer, Integer> ss =Tests.seriesTest(testData);
        Pair[] thresholds = new Pair[]
                {new Pair(2315, 2685), new Pair(1113, 1386), new Pair(527, 723),
                new Pair(240, 384), new Pair(103, 209), new Pair(103, 209)};
        boolean results = true;
        for(int i =0; i < 6; i++)
        {
            if(ss.get(i+1) >= thresholds[i].min && ss.get(i+1) <= thresholds[i].max){
                series[i].setTextFill(Color.BLACK);
                series[i].setText((i+1)+" : " + ss.get(i+1));
            }
            else {
                series[i].setTextFill(Color.DARKRED);
                series[i].setText((i+1)+" : " + ss.get(i+1));
                results = false;
            }
        }

        if(results){
            seriesLabel.setTextFill(Color.DARKGREEN);
            seriesLabel.setText("Zaliczone");
        }
        else{
            seriesLabel.setTextFill(Color.DARKRED);
            seriesLabel.setText("Niepowodzenie");
        }

    }

    private void testLongSeries() {
        int longSeriesCount = Tests.longSeriesTest(testData);
        if(longSeriesCount>0){
            longSLabel.setTextFill(Color.DARKRED);
            longSLabel.setText("Niepowodzenie");
            longSeriesResult.setTextFill(Color.DARKRED);
        }
        else{
            longSLabel.setTextFill(Color.DARKGREEN);
            longSLabel.setText("Zaliczone");
            longSeriesResult.setTextFill(Color.BLACK);
        }
        longSeriesResult.setText(Integer.toString(longSeriesCount));
    }

    private void testPoker() {
        double result = Tests.pokerTest(testData);
        if(result > 2.16 && result < 46.17){
            pokerLabel.setTextFill(Color.DARKGREEN);
            pokerLabel.setText("Zaliczone");
            pokerResult.setTextFill(Color.BLACK);
        }
        else{
            pokerLabel.setTextFill(Color.DARKRED);
            pokerLabel.setText("Niepowodzenie");
            pokerResult.setTextFill(Color.DARKRED);
        }
        result = DoubleRounder.round(result,2);

        pokerResult.setText(Double.toString(result).replace('.',','));
    }

    @FXML
    private void onSave(ActionEvent actionEvent){
        String output = "";

        output = "RESULTS FROM FIPS 140-2 TESTS |\n" +
                "______________________________|\n" +
                "\n" +
                "SINGLE BIT TEST - " + (singleBitLabel.getText() == "Zaliczone" ? "SUCCESS" : "FAILED") + "\n" +
                "+------------+------------+-----------+\n" +
                "| MIN VALUE  |   RESULT   | MAX VALUE |\n" +
                "+------------+------------+-----------+\n" +
                "|    9750    |    " + singleBitsResult.getText() +
                (singleBitsResult.getText().length() == 5 ? "   " : "   ") + "|   10250   |\n" +
                "+------------+------------+-----------+\n" +
                "\n" +
                "SERIES TEST - " + (seriesLabel.getText() == "Zaliczone" ? "SUCCESS" : "FAILED") + "\n" +
                "+----------+-------------+-------------+-------------+-------------+-------------+-------------+\n" +
                "|          |      1      |      2      |      3      |      4      |      5      |    6=<      |\n" +
                "+----------+-------------+-------------+-------------+-------------+-------------+-------------+\n" +
                "| EXPECTED | 2315 - 2685 | 1113 - 1386 |  527 - 723  |  240 - 384  |  103 - 209  |  103 - 209  |  \n" +
                "+----------+-------------+-------------+-------------+-------------+-------------+-------------+\n" +
                "|  ACTUAL  |" + prepareString(series[0].getText().substring(4), 13)+"|" +
                prepareString(series[1].getText().substring(4), 13)+"|" +
                prepareString(series[2].getText().substring(4), 13)+"|" +
                prepareString(series[3].getText().substring(4), 13)+"|" +
                prepareString(series[4].getText().substring(4), 13)+"|" +
                prepareString(series[5].getText().substring(4), 13)+"|\n" +
                "+----------+-------------+-------------+-------------+-------------+-------------+-------------+\n" +
                "\n" +
                "LONG SERIES TEST - " + (longSLabel.getText() == "Zaliczone" ? "SUCCESS" : "FAILED") + "\n" +
                "+----------+---------+\n" +
                "| EXPECTED |    0    |\n" +
                "+----------+---------+\n" +
                "|  ACTUAL  |" + prepareString(longSeriesResult.getText(),9) + "|\n" +
                "+----------+---------+\n" +
                "\n" +
                "POKER TEST - " + (pokerLabel.getText() == "Zaliczone" ? "SUCCESS" : "FAILED") +"\n"+
                "+------------+------------+-----------+\n" +
                "| MIN VALUE  |   RESULT   | MAX VALUE |\n" +
                "+------------+------------+-----------+\n" +
                "|    2,16    |" + prepareString(pokerResult.getText(),12) + "|   46,17   |\n" +
                "+------------+------------+-----------+";
        FileHandler.saveToFile(output);
    }

    private String prepareString(String text, int len) {
        int ctr = 0;
        while(text.length()<len){
            if(ctr % 2 == 0){
                text = " " + text;
                ctr ++;
            }
            else {
                text += " ";
                        ctr++;
            }

        }
        return text;
    }

    @FXML
    private void onBitCheck (ActionEvent actionEvent) {
        if(bitBox.isSelected()) {
            if(seriesBox.isSelected() && longSBox.isSelected() && pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
        else{
            if(!seriesBox.isSelected() && !longSBox.isSelected() && !pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
    }

    @FXML
    private void onSeriesCheck (ActionEvent actionEvent) {
        if(seriesBox.isSelected()) {
            if(bitBox.isSelected() && longSBox.isSelected() && pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
        else{
            if(!bitBox.isSelected() && !longSBox.isSelected() && !pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
    }

    @FXML
    private void onLongSCheck (ActionEvent actionEvent) {
        if(longSBox.isSelected()) {
            if(bitBox.isSelected() && seriesBox.isSelected() && pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
        else{
            if(!bitBox.isSelected() && !seriesBox.isSelected() && !pokerBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
    }

    @FXML
    private void onPokerCheck (ActionEvent actionEvent) {
        if(pokerBox.isSelected()) {
            if(bitBox.isSelected() && longSBox.isSelected() && seriesBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
        else{
            if(!bitBox.isSelected() && !longSBox.isSelected() && !seriesBox.isSelected()){
                onSelectAll(actionEvent);
            }
        }
    }
}
