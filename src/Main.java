import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Observable;

public class Main extends Application {

    private ArrayList<Robot> robots = new ArrayList<>();
    private int selected = 0;

    public static void main(String[] args) {
        launch(Main.class);
    }

    public void start(Stage stage) {

        //Window characteristics: size/name
        stage.setHeight(700);
        stage.setWidth(900);
        stage.setResizable(false);

        System.out.println("try");

        stage.setTitle("Tunshu console");

        addRobot("No robot selected", 999);
        addRobot("Boebot 2", "COM8");
        addRobot("Boebot 2", "COM8");

        //Application main layout
        BorderPane mainWindowLayout = new BorderPane();

        Scene mainView = new Scene(mainWindowLayout);

        mainWindowLayout.setTop(topBar());
        mainWindowLayout.setCenter(botControl());
        mainWindowLayout.setLeft(botList());

        stage.setScene(mainView);

        stage.show();

    }

    private Node botControl() {

        BorderPane centerControlLayout = new BorderPane();

        Label tunshuName = new Label(this.robots.get(this.selected).getName());
        tunshuName.setStyle("-fx-font-size: 42");

        GridPane buttonControlLayout = new GridPane();
        Button driveForward = new Button();
        Button driveLeft = new Button();
        Button driveRight = new Button();
        Button driveBack = new Button();
        Button brake = new Button();

        //buttonControlLayout.setPadding( new Insets(50,50,50,50) );
        buttonControlLayout.setHgap(10.0);
        buttonControlLayout.setVgap(10.0);

        driveForward.setMinSize(100.0, 100.0);
        driveLeft.setMinSize(100.0, 100.0);
        driveRight.setMinSize(100.0, 100.0);
        driveBack.setMinSize(100.0, 100.0);
        brake.setMinSize(320.0, 100.0);

        buttonControlLayout.add(driveForward, 1, 0);
        buttonControlLayout.add(driveLeft, 0, 1);
        buttonControlLayout.add(driveRight, 2, 1);
        buttonControlLayout.add(driveBack, 1, 1);
        buttonControlLayout.add(brake, 0, 2, 3, 1);


        centerControlLayout.setTop(tunshuName);
        centerControlLayout.setCenter(buttonControlLayout);

        return centerControlLayout;

    }

    private Node topBar() {

        Menu menuBotManagement = new Menu("Bot Management");
        MenuItem menuItemAddBot = new MenuItem("Add Bot");
        MenuItem menuItemDeleteBot = new MenuItem("Delete Bot");
        menuBotManagement.getItems().addAll(menuItemAddBot, menuItemDeleteBot);

        Menu menuHelp = new Menu("Help");
        MenuItem menuItemHelp = new MenuItem("Help?");
        MenuItem menuItemAbout = new MenuItem("About");
        menuHelp.getItems().addAll(menuItemHelp, menuItemAbout);

        MenuBar topBar = new MenuBar();
        topBar.getMenus().addAll(menuBotManagement, menuHelp);

        return topBar;

    }

    public Node botList() {
        ListView listView = new ListView();
        for (Robot bot : this.robots) {
            if (!bot.getName().equals("No robot selected")) {
                listView.getItems().add(botGUI(bot));
            }
        }
        this.selected = listView.getSelectionModel().getSelectedIndex();
        VBox vBox = new VBox();
        vBox.getChildren().add(new Label("Bot status"));
        vBox.getChildren().add(listView);


        return vBox;
    }

    private Object botGUI(Robot bot) {
        VBox vBox = new VBox();
        vBox.getChildren().add(new Label(bot.getName()));
        vBox.getChildren().add(new Label("Status: " + bot.getStatus()));
        vBox.getChildren().add(new Label("Current task: " + bot.getCurrentTask()));
        return vBox;
    }

    public void addRobot(String name, String com) {
        this.robots.add(new Robot(name, com));
    }

    public void addRobot(String name, int com) {
        this.robots.add(new Robot(name, com));
    }
}
