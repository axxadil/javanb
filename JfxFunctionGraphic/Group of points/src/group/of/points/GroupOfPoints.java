package jfxfunctiongraphic;

import java.awt.event.TextListener;
import java.util.function.DoubleUnaryOperator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class GroupOfPoints extends Application{
    
    @Override
public void start(Stage primaryStage){

BorderPane root = new BorderPane();
Scene scene = new Scene(root, 750, 350, Color.LIGHTCYAN);

primaryStage.setTitle("График функции");
primaryStage.setScene(scene);

HBox taskbar = new HBox(10);
HBox bottom = new HBox(8);
taskbar.setPadding(new Insets(10, 30, 50, 30));
bottom.setPadding(new Insets(10,10,10,10));
taskbar.setPrefHeight(50);
taskbar.setAlignment(Pos.TOP_LEFT);

root.setTop(taskbar);
root.setBottom(bottom);
bottom.getChildren().add(createComboBoxNew(lb1,1));
bottom.getChildren().add(createComboBoxNew(lb2,2));
bottom.getChildren().add(createComboBoxNew(lb3,3));

taskbar.getChildren().add(createComboBox());
taskbar.getChildren().add(createButton("icon2.png", new Runnable(){
    private String Line;
    @Override
    public void run(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        //xAxis.setLayoutX(xmin);
        LineChart<Number, Number>chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(Line);
        xAxis.setLabel("OX");
        yAxis.setLabel("OY");
        xmax = Integer.parseInt(maxX.getText()); 
        xmin = Integer.parseInt(minX.getText());
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(cb.getValue().toString());
        double lowX = xmin*Math.PI;
        double highX = xmax*Math.PI;
        
        int steps = 50;
        double dx = (highX- lowX)/(steps-1);
        for (int i=0; i < steps; i++){
            double x = lowX + i*dx;
            double y = func.applyAsDouble(x);
            series.getData().add(new XYChart.Data<>(x, y));
        }
        chart.getData().add(series);
        
        changeView(chart);
    }
}));
aTextField = textInput(bottom,"a:","1");
bTextField = textInput(bottom,"b:","1");
cTextField = textInput(bottom,"c:","1");
aTextField.setMaxWidth(48);
bTextField.setMaxWidth(48);
cTextField.setMaxWidth(48);

k1 = textInput(bottom,"k1","1");
k2 = textInput(bottom,"k2","1");
k3 = textInput(bottom,"k3","1");
k1.setMaxWidth(48);
k2.setMaxWidth(48);
k3.setMaxWidth(48);
minX = textInput(taskbar,"X min","-2");
maxX = textInput(taskbar,"X max","2");
view = new StackPane();
root.setCenter(view);
view.getChildren().add(new Text("Используем Java FX..."));
primaryStage.show();
}
    public static void main(String[] args) {
        launch(args);
    }
    private Node createButton(String iconName, final Runnable action){
        final ImageView node = new ImageView(new Image("file:" + iconName));
        node.setOnMouseClicked(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event){
    action.run();
}
    });
        return node;
    }
    
    private TextField textInput(HBox h,String lab,String text){
        TextField ti = new TextField(text);
        Label lt = new Label(lab);
        h.getChildren().add(lt);
        h.getChildren().add(ti);
        return ti;
    }
    

    private void changeView(Node node){
        view.getChildren().clear();
        view.getChildren().add(node);
    }
    
    private Node createComboBox(){
        
        cb.getItems().addAll("sin x","cos x","x^2","x^3","tg x","a(f1(x))^k1 + b(f2(x))^k + c(f3(x))^k3");
        cb.getSelectionModel().selectFirst();
        
        cmbFunc = new DoubleUnaryOperator[6];
        cmbFunc[0] = Math::sin;
        cmbFunc[1] = Math::cos;
        cmbFunc[2] = x -> x*x;
        cmbFunc[3] = x -> x*x*x;
        cmbFunc[4] = Math::tan;
        cmbFunc[5] = (x) -> {
            double tf1,tf2,tf3;
            tf1 = func1.applyAsDouble(x);
            tf1 = Math.pow(tf1,Integer.parseInt(k1.getText()));
            tf1 = tf1*Double.parseDouble(aTextField.getText());
            
            tf2 = func2.applyAsDouble(x);
            tf2 = Math.pow(tf2,Integer.parseInt(k2.getText()));
            tf2 = tf2*Double.parseDouble(bTextField.getText());
            
            tf3 = func3.applyAsDouble(x);
            tf3 = Math.pow(tf3,Integer.parseInt(k3.getText()));
            tf3 = tf3*Double.parseDouble(cTextField.getText());
            
            return tf1+tf2+tf3;};
        func = cmbFunc[0];
        
        cb.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                      func = cmbFunc[cb.getSelectionModel().getSelectedIndex()];
            }
        });
        return cb;
    }
    
    private Node createComboBoxNew(ComboBox<String> temp, int selectFunc){
        int sF = selectFunc;
        temp.getItems().addAll("sin x","cos x","x^2","x^3","tg x","x/2");
        temp.getSelectionModel().selectFirst();
        
        cmbFuncNew = new DoubleUnaryOperator[6];
        cmbFuncNew[0] = Math::sin;
        cmbFuncNew[1] = Math::cos;
        cmbFuncNew[2] = x -> x*x;
        cmbFuncNew[3] = x -> x*x*x;
        cmbFuncNew[4] = Math::tan;
        cmbFuncNew[5] = x -> x/2;
        
        func1 = cmbFuncNew[0];
        func2 = cmbFuncNew[0];
        func3 = cmbFuncNew[0];
        
        temp.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                switch(sF){
                    case 1:func1 = cmbFuncNew[temp.getSelectionModel().getSelectedIndex()];
                        break;
                    case 2:func2 = cmbFuncNew[temp.getSelectionModel().getSelectedIndex()];
                        break;
                    case 3:func3 = cmbFuncNew[temp.getSelectionModel().getSelectedIndex()];
                        break;
                    default:{func1 = cmbFuncNew[0];
                            func2 = cmbFuncNew[0];
                            func3 = cmbFuncNew[0];
                    }
                }
                
            }
        });
        return temp;
    }
    private ComboBox<String> cb = new ComboBox<>();
    private ComboBox<String> lb1 = new ComboBox<>();
    private ComboBox<String> lb2 = new ComboBox<>();
    private ComboBox<String> lb3 = new ComboBox<>();
    
    private TextField minX,maxX,aTextField,bTextField,cTextField,k1,k2,k3;
    
    private int xmin=-2,xmax=2;
    private StackPane view;
    DoubleUnaryOperator[] cmbFunc,cmbFuncNew;
    DoubleUnaryOperator func,func1,func2,func3;
}

