package sch.frog.kit.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import sch.frog.kit.MainController;
import sch.frog.kit.util.StringUtils;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class TimeToolView extends CustomViewControl implements Initializable {

    @FXML
    private TextField currentDate;

    @FXML
    private TextField currentTimestamp;

    private Timer timer;

    private void initTimer(){
        timer = new Timer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Date current = new Date();
                String date = sdf.format(current);
                currentDate.setText(date);
                currentTimestamp.setText(String.valueOf(current.getTime()));
            }
        }, 1000, 1000);
    }

    @FXML
    public void startTimer(){
        if(this.timer == null){
            this.initTimer();
        }
    }

    @FXML
    public void stopTimer(){
        if(this.timer != null){
            this.timer.cancel();
            this.timer = null;
        }
    }

    @FXML
    private ComboBox<String> timestampUnit;

    @FXML
    private TextField timestampInput;

    @FXML
    private TextField dateResult;

    @FXML
    public void convertToDate(){
        dateResult.setText(null);
        String unit = timestampUnit.getSelectionModel().getSelectedItem();
        if(StringUtils.isBlank(unit)){
            MainController.error("please select timestamp unit");
            return;
        }
        String text = timestampInput.getText();
        try{
            long timestamp = Long.parseLong(text);
            if("s".equals(unit)){
                timestamp = timestamp * 1000;
            }
            Date date = new Date();
            date.setTime(timestamp);
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            dateResult.setText(format);
        }catch (NumberFormatException e){
            MainController.error("timestamp input is not a number");
        }
    }

    @FXML
    private ComboBox<String> outputUnit;

    @FXML
    private TextField dateInput;

    @FXML
    private TextField timestampResult;

    @FXML
    public void convertToTimestamp(){
        timestampResult.setText(null);
        String unit = outputUnit.getSelectionModel().getSelectedItem();
        if(StringUtils.isBlank(unit)){
            MainController.error("please select output unit");
            return;
        }
        String dateInputText = dateInput.getText();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateInputText);
            long timestamp = date.getTime();
            if("s".equals(unit)){
                timestamp = timestamp / 1000;
            }
            timestampResult.setText(String.valueOf(timestamp));
        } catch (ParseException e) {
            MainController.error("date format incorrect, " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SingleSelectionModel<String> timestampUnitSelectionModel = timestampUnit.getSelectionModel();
        SingleSelectionModel<String> outputUnitSelectionModel = outputUnit.getSelectionModel();
        timestampUnitSelectionModel.select(0);
        outputUnitSelectionModel.select(0);

        timestampUnitSelectionModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                dateResult.setText(null);
            }else if(!newVal.equals(oldVal) && StringUtils.isNotBlank(timestampInput.getText())){
                convertToDate();
            }
        });

        outputUnitSelectionModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                timestampResult.setText(null);
            }else if(!newVal.equals(oldVal) && StringUtils.isNotBlank(dateInput.getText())){
                convertToTimestamp();
            }
        });
        initTimer();
    }

    @Override
    public void onClose() {
        this.stopTimer();
    }

    @Override
    public void onHidden() {
        this.stopTimer();
    }

    @Override
    public void onShow() {
        this.startTimer();
    }
}
