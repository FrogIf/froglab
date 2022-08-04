package sch.frog.kit.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.server.handle.annotation.RequestAction;
import sch.frog.kit.server.handle.annotation.RequestParam;
import sch.frog.kit.common.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeToolView extends CustomViewControl {

    @FXML
    private TextField currentDate;

    @FXML
    private TextField currentTimestamp;

    @FXML
    private Button timeCtlBtn;

    private Timer timer;

    private void initTimer(){
        timer = new Timer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Date current = new Date();
                String date = sdf.format(current);
                Platform.runLater(() -> {
                    currentDate.setText(date);
                    currentTimestamp.setText(String.valueOf(current.getTime()));
                });
            }
        }, 1000, 1000);
    }

    @FXML
    public void startOrStopTimer(){
        if(this.timer == null){ // start
            this.startTimer();
        }else{ // stop
            this.stopTimer();
        }
    }

    private void startTimer(){
        if(this.timer == null){
            this.initTimer();
            this.timeCtlBtn.setText("pause");
        }
    }

    private void stopTimer(){
        if(this.timer != null){
            this.timer.cancel();
            this.timer = null;
            this.timeCtlBtn.setText("start");
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
        if(StringUtil.isBlank(unit)){
            LogKit.error("please select timestamp unit");
            return;
        }
        String text = timestampInput.getText();
        try{
            final long timestamp = Long.parseLong(text);
            String date = timestampToDate(timestamp, unit);
            dateResult.setText(date);
            LogKit.info(timestamp + unit + " -> " + date);
        }catch (NumberFormatException e){
            LogKit.error("timestamp input is not a number");
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
        if(StringUtil.isBlank(unit)){
            LogKit.error("please select output unit");
            return;
        }
        try {
            String dateInputText = dateInput.getText();
            long timestamp = dateToTimestamp(dateInputText, unit);
            LogKit.info(dateInputText + " -> " + timestamp + unit);
            timestampResult.setText(String.valueOf(timestamp));
        } catch (IllegalArgumentException e) {
            LogKit.error(e.getMessage());
        }
    }

    @FXML
    private DatePicker datePickerA1;

    @FXML
    private DatePicker datePickerA2;

    @FXML
    private ComboBox<String> subResultUnit;

    @FXML
    private TextField dateSubDateResult;

    @FXML
    public void onExecuteDateSub(){
        dateSub();
    }

    private void dateSub(){
        dateSubDateResult.setText(null);
        LocalDate date1 = datePickerA1.getValue();
        LocalDate date2 = datePickerA2.getValue();
        if(date1 != null && date2 != null){
            String unit = subResultUnit.getSelectionModel().getSelectedItem();
            long sub = dateInterval(date1, date2, unit);
            LogKit.info(date2 + " - " + date1 + " = " + sub + unit);
            dateSubDateResult.setText(String.valueOf(sub));
        }
    }

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private ComboBox<String> offsetUnit;

    @FXML
    private TextField offsetResult;

    @FXML
    private TextField dateOffsetValue;

    @FXML
    public void onExecuteDateOffset(){
        dateOffset();
    }

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void dateOffset(){
        offsetResult.setText(null);
        LocalDate date = datePickerStart.getValue();
        String offsetVal = dateOffsetValue.getText();
        if(StringUtil.isBlank(offsetVal)){
            return;
        }
        long offset = 0;
        try{
            offset = Long.parseLong(offsetVal);
        }catch (NumberFormatException e){
            LogKit.error("offset input is not a number");
            return;
        }
        if(date != null){
            String unit = offsetUnit.getSelectionModel().getSelectedItem();
            String result = dateOffset(date, offset, unit);
            LogKit.info(date + " + " + offsetVal + unit + " = " + result);
            offsetResult.setText(result);
        }
    }

    @Override
    public void init() {
        initTimer();

        SingleSelectionModel<String> outputUnitSelectionModel = outputUnit.getSelectionModel();
        outputUnitSelectionModel.select(0);
        outputUnitSelectionModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                timestampResult.setText(null);
            }else if(!newVal.equals(oldVal) && StringUtil.isNotBlank(dateInput.getText())){
                convertToTimestamp();
            }
        });

        SingleSelectionModel<String> timestampUnitSelectionModel = timestampUnit.getSelectionModel();
        timestampUnitSelectionModel.select(0);
        timestampUnitSelectionModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                dateResult.setText(null);
            }else if(!newVal.equals(oldVal) && StringUtil.isNotBlank(timestampInput.getText())){
                convertToDate();
            }
        });

        SingleSelectionModel<String> subResultUnitSelectModel = subResultUnit.getSelectionModel();
        subResultUnitSelectModel.select("day");
        subResultUnitSelectModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                dateSubDateResult.setText(null);
            }else if(!newVal.equals(oldVal)){
                dateSub();
            }
        });

        SingleSelectionModel<String> offsetUnitSelectionModel = offsetUnit.getSelectionModel();
        offsetUnitSelectionModel.select("day");
        offsetUnitSelectionModel.selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal == null){
                offsetResult.setText(null);
            }else if(!newVal.equals(oldVal)){
                dateOffset();
            }
        });

        StringConverter<LocalDate> datePickerConverter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if(date != null){
                    return dateFormatter.format(date);
                }else{
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(StringUtil.isNotBlank(string)){
                    return LocalDate.parse(string, dateFormatter);
                }else{
                    return null;
                }
            }
        };

        datePickerA1.setConverter(datePickerConverter);
        datePickerA2.setConverter(datePickerConverter);
        datePickerStart.setConverter(datePickerConverter);
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

    @RequestAction(path = "/time/timestampToDate", description = "timestamp convert to date")
    public String timestampToDate(@RequestParam(name = "timestamp", description = "origin timestamp") long timestamp,
                                  @RequestParam(name = "unit", description = "unit") String unit){
        long millis = timestamp;
        if("s".equals(unit)){
            millis = millis * 1000;
        }
        Date date = new Date();
        date.setTime(millis);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @RequestAction(path = "/time/dateToTimestamp", description = "date convert to timestamp")
    public long dateToTimestamp(@RequestParam(name = "date", description = "origin date") String date,
                                @RequestParam(name = "unit", description = "unit") String unit){
        try {
            Date dateObj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            long timestamp = dateObj.getTime();
            if("s".equals(unit)){
                timestamp = timestamp / 1000;
            }
            return timestamp;
        } catch (ParseException e) {
            throw new IllegalArgumentException("date format incorrect, " + e.getMessage());
        }
    }

    @RequestAction(path = "/time/dateInterval", description = "get interval for two different date")
    public long dateInterval(@RequestParam(name = "begin", description = "begin date") LocalDate begin,
                             @RequestParam(name = "end", description = "end date") LocalDate end,
                             @RequestParam(name = "unit", description = "unit") String unit){
        long sub = 0;
        if("year".equals(unit)){
            Period period = begin.until(end);
            sub = period.getYears();
        }else if("month".equals(unit)){
            Period period = begin.until(end);
            sub = period.toTotalMonths();
        }else{
            long from = begin.toEpochDay();
            long to = end.toEpochDay();
            sub = to - from;
            switch (unit){
                case "day":
                    // do nothing
                    break;
                case "week":
                    sub = sub / 7;
                    break;
                case "hour":
                    sub = sub * 24;
                    break;
                case "minute":
                    sub = sub * 24 * 60;
                    break;
                case "s":
                    sub = sub * 24 * 60 * 60;
                    break;
                case "ms":
                    sub = sub * 24 * 60 * 60 * 1000;
                    break;
                default:
                    sub = Long.MIN_VALUE;
                    break;
            }
        }
        return sub;
    }

    @RequestAction(path = "/time/dateOffset", description = "get a new date by date offset")
    public String dateOffset(@RequestParam(name = "date", description = "origin date") LocalDate date,
                             @RequestParam(name = "offset", description = "offset") long offset,
                             @RequestParam(name = "unit", description = "unit") String unit){
        LocalDate result = null;
        if("year".equals(unit)){
            result = date.plusYears(offset);
        }else if("month".equals(unit)){
            result = date.plusMonths(offset);
        }else {
            switch (unit){
                case "day":
                    // do nothing
                    break;
                case "week":
                    offset = offset * 7;
                    break;
                case "hour":
                    offset = offset / 24;
                    break;
                case "minute":
                    offset = offset / 24 / 60;
                    break;
                case "s":
                    offset = offset / 24 / 60 / 60;
                    break;
                case "ms":
                    offset = offset / 24 / 60 / 60 / 1000;
                    break;
                default:
                    offset = Long.MIN_VALUE;
                    break;
            }
            if(offset != Long.MIN_VALUE){
                result = date.plusDays(offset);
            }
        }
        if(result != null){
            return result.format(dateTimeFormatter);
        }else{
            return null;
        }
    }
}


