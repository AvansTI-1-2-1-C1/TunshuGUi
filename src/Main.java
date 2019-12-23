import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
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
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setResizable(false);

        System.out.println("try");

        stage.setTitle("Tunshu console");

        addRobot("No robot selected", 999);
        addRobot("Boebot 2", "COM8");
        addRobot("Boebot 2", "COM8");

        //Application main layout
        BorderPane mainWindowLayout = new BorderPane();

        Scene mainView = new Scene(mainWindowLayout);

        mainView.getStylesheets().add("listView.css");

        mainWindowLayout.setTop(topBar());
        mainWindowLayout.setCenter(botControl());
        mainWindowLayout.setLeft(botList());

        stage.setScene(mainView);

        stage.show();

    }

    private Node botControl() {

        /*

        RightModules layout:

        centerControlLayout(VBox)
            -insideCenterControlLayoutUp(HBox)
                -informationContainer(HBox)
                -botFunNavContainer(VBox)
                    -botFunctionsContainer(HBox)
                    -botNavContainer(HBox)

            -insideCenterControlLayoutDown(HBox)
                -bottomControlContainer(HBox)

        Containers are shown in order.

        See technical document for detailed graphical layout of the GUI.

         */

        VBox centerControlLayout = new VBox();
        centerControlLayout.setMinSize(514,560);
        centerControlLayout.setMaxSize(514,560);
        centerControlLayout.setStyle("-fx-background-color: #121212;");

        HBox insideCenterControlLayoutUp =  new HBox();
        insideCenterControlLayoutUp.setMinSize(484,242);
        insideCenterControlLayoutUp.setMaxSize(484,242);
        VBox.setMargin( insideCenterControlLayoutUp, new Insets(18,0,0,10));

        HBox informationContainer = new HBox();
        informationContainer.setMinSize(230,242);
        informationContainer.setMaxSize(230,242);
        informationContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        informationContainer.setAlignment(Pos.TOP_CENTER);

        Label informationTitle = new Label("Information");
        informationTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        informationTitle.setTextFill(Color.WHITE);
        informationTitle.setPadding(new Insets( 5, 0,5,0));

        informationContainer.getChildren().add(informationTitle);

        VBox botFunNavContainer = new VBox();
        botFunNavContainer.setMinSize(236,242);
        botFunNavContainer.setMaxSize(236,242);
        HBox.setMargin( botFunNavContainer, new Insets(0,0,0,18));

        VBox botFunctionsContainer = new VBox();
        botFunctionsContainer.setMinSize(236, 112);
        botFunctionsContainer.setMaxSize(236,112);
        botFunctionsContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        //VBox.setMargin( botFunctionsContainer, new Insets(0,0,0,0));
        botFunctionsContainer.setAlignment(Pos.TOP_CENTER);

        Label functionsTitle = new Label("Functions");
        functionsTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        functionsTitle.setTextFill(Color.WHITE);
        functionsTitle.setPadding(new Insets( 5, 0,5,0));

        botFunctionsContainer.getChildren().add(functionsTitle);
        //botFunctionsContainer.getChildren().add(onOffLights());
        botFunctionsContainer.getChildren().add(muteUnMuteSound());
        botFunctionsContainer.getChildren().add(speedSlider());

        HBox botNavContainer = new HBox();
        botNavContainer.setMinSize(236, 112);
        botNavContainer.setMaxSize(236,112);
        botNavContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        VBox.setMargin( botNavContainer, new Insets(18,0,0,0));
        botNavContainer.setAlignment(Pos.TOP_CENTER);

        Label navigationTitle = new Label("Navigation");
        navigationTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        navigationTitle.setTextFill(Color.WHITE);
        navigationTitle.setPadding(new Insets( 5, 0,5,0));

        botNavContainer.getChildren().add(navigationTitle);

        HBox insideCenterControlLayoutDown =  new HBox();
        insideCenterControlLayoutDown.setMinSize(484,242);
        insideCenterControlLayoutDown.setMaxSize(484,242);
        insideCenterControlLayoutDown.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        VBox.setMargin( insideCenterControlLayoutDown, new Insets(18,0,0,10));
        insideCenterControlLayoutDown.setAlignment(Pos.TOP_CENTER);

        Label controlTitle = new Label("Manual Control");
        controlTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        controlTitle.setTextFill(Color.WHITE);
        controlTitle.setPadding(new Insets( 10, 0,5,0));

        VBox bottomControlContainer = new VBox();
        bottomControlContainer.setAlignment(Pos.TOP_CENTER);

        bottomControlContainer.getChildren().add(controlTitle);

        botFunNavContainer.getChildren().addAll(botFunctionsContainer,botNavContainer);

        insideCenterControlLayoutUp.getChildren().addAll(informationContainer, botFunNavContainer);
        insideCenterControlLayoutDown.getChildren().addAll(bottomControlContainer);

        centerControlLayout.getChildren().addAll(insideCenterControlLayoutUp, insideCenterControlLayoutDown);

        Label tunshuName = new Label(this.robots.get(this.selected).getName());
        tunshuName.setStyle("-fx-font-size: 42");
        tunshuName.setTextFill(Color.WHITE);

        GridPane buttonControlLayout = new GridPane();
        Button driveForward = new Button("");

        driveForward.setOnAction( event -> {

            //GuiLogic....

        } );

        Button driveLeft = new Button("");
        Button driveRight = new Button("");
        Button driveBack = new Button("");
        Button brake = new Button();
        Button emergencyBrake = new Button();
        Button mute = new Button();
        Button lineFollower = new Button();

        //#111111 : grey
        driveForward.setStyle("-fx-background-color: #1F1826");
        driveLeft.setStyle("-fx-background-color: #1F1826");
        driveRight.setStyle("-fx-background-color: #1F1826");
        driveBack.setStyle("-fx-background-color: #1F1826");
        brake.setStyle("-fx-background-color: #1F1826");
        emergencyBrake.setStyle("-fx-background-color: #DF1D1D");
        mute.setStyle("-fx-background-color: black");
        lineFollower.setStyle("-fx-background-color: black");

        driveForward.setOnMouseEntered(e -> driveForward.setStyle("-fx-background-color: #242424; "));
        driveForward.setOnMouseExited(e -> driveForward.setStyle("-fx-background-color: #1F1826;"));

        //buttonControlLayout.setPadding( new Insets(50,50,50,50) );
        buttonControlLayout.setHgap(10.0);
        buttonControlLayout.setVgap(10.0);

        driveForward.setMinSize(60.0,60.0);
        driveLeft.setMinSize(60.0,60.0);
        driveRight.setMinSize(60.0,60.0);
        driveBack.setMinSize(60.0,60.0);
        brake.setMinSize(126, 60.0);
        brake.setMaxSize(126, 60.0);

        emergencyBrake.setMinSize(70.0,70.0);
        mute.setMinSize(70.0,70.0);
        lineFollower.setMinSize(70.0,70.0);

        buttonControlLayout.add(driveForward,1, 0);
        buttonControlLayout.add(driveLeft, 0, 1);
        buttonControlLayout.add(driveRight, 2, 1);
        buttonControlLayout.add(driveBack, 1, 1);
        buttonControlLayout.add(brake, 4,1,3,1);
//        buttonControlLayout.add(emergencyBrake,0,8);
//        buttonControlLayout.add(lineFollower,1,8);
//        buttonControlLayout.add(mute,2,8);

        buttonControlLayout.setHalignment(emergencyBrake, HPos.CENTER);
        buttonControlLayout.setHalignment(lineFollower, HPos.CENTER);
        buttonControlLayout.setHalignment(mute, HPos.CENTER);

        buttonControlLayout.setPadding(new Insets(35,0,0,0));

        //centerControlLayout.setTop(tunshuName);
        bottomControlContainer.getChildren().add(buttonControlLayout);
        //centerControlLayout.setCenter(bottomControlContainer);

        //centerControlLayout.getChildren().addAll(informationContainer, botFunctionsContainer, botNavContainer, bottomControlContainer);

        return centerControlLayout;

    }

    private Node speedSlider() {

        HBox speedSliderContainer = new HBox();
        speedSliderContainer.setPadding( new Insets(0,0,0,15));

        Label speedSliderTitle = new Label("Speed");
        speedSliderTitle.setTextFill(Color.WHITE);
        speedSliderTitle.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        speedSliderTitle.setPadding(new Insets(3,0,0,2));

        Slider speedSlider = new Slider();
        speedSlider.setMax(100.0);
        speedSlider.setMin(0.0);
        speedSlider.setValue(40.0);
        speedSlider.setPadding( new Insets(3,0,0,5));

        speedSliderContainer.getChildren().addAll(speedSliderTitle, speedSlider);

        return speedSliderContainer;
    }

    private Node muteUnMuteSound() {

        VBox soundMuteContainer = new VBox();
        soundMuteContainer.setPadding( new Insets(0,0,0,15));

//        Label soundMuteTitle = new Label("Sound");
//        soundMuteTitle.setTextFill(Color.WHITE);
//        soundMuteTitle.setStyle("-fx-font-size: 12; -fx-font-family: 'Helvetica';");

        ToggleGroup soundAndKLightsTGroup = new ToggleGroup();
        RadioButton muteUnMuteButton = new RadioButton("Sound");
        RadioButton onOffLightsButton = new RadioButton("Lights ");

        muteUnMuteButton.setToggleGroup(soundAndKLightsTGroup);
        onOffLightsButton.setToggleGroup(soundAndKLightsTGroup);

        soundMuteContainer.getChildren().addAll(muteUnMuteButton, onOffLightsButton);


        return soundMuteContainer;
    }

    private Node topBar() {

        HBox topBar = new HBox();
        topBar.setMinHeight(40.0);

        Label consoleTitle = new Label("Tunshu Console");
        consoleTitle.setTextFill(Color.WHITE);
        consoleTitle.setStyle("-fx-font-size: 20; -fx-font-family: 'Helvetica';");
        consoleTitle.setPadding( new Insets(0,0,0,15));

        topBar.getChildren().addAll(consoleTitle);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.setStyle("-fx-background-color: black;");

        return topBar;

    }

    public Node botList() {
        ListView listView = new ListView();
        listView.getStyleClass().add("listView.css");
        listView.setMinHeight(450.0);
        listView.setMinWidth(263.0);
        listView.setMaxHeight(450);
        listView.setMaxWidth(263.0);
        //listView.setFixedCellSize(90);
        //listView.setPadding(new Insets(52,0,0,0));


        for (Robot bot : this.robots) {
            if (!bot.getName().equals("No robot selected")) {
                listView.getItems().add(botGUI(bot));
            }
        }

        listView.getItems().add(addBotButton());
        this.selected = listView.getSelectionModel().getSelectedIndex();

        Label botListTitle = new Label("Bot List");
        botListTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        botListTitle.setTextFill(Color.WHITE);
        botListTitle.setPadding(new Insets( 5, 0,5,0));


        VBox vBox = new VBox();
        vBox.getChildren().add(botListTitle);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().add(listView);

        vBox.setMinHeight(502.0);
        vBox.setMinWidth(263.0);
        vBox.setMaxSize(263.0, 522.0);
        VBox.setMargin( vBox, new Insets(18,0,0,15));
        vBox.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F");

        VBox leftSideVBoxContainer = new VBox();
        leftSideVBoxContainer.setMinHeight(560.0);
        leftSideVBoxContainer.setMinWidth(286.0);
        leftSideVBoxContainer.getChildren().add(vBox);
        leftSideVBoxContainer.setStyle(" -fx-background-color: #121212");


        return leftSideVBoxContainer;
    }

    private Object botGUI(Robot bot) {
        VBox vBox = new VBox();
        vBox.getChildren().add(new Label(bot.getName()));
        vBox.getChildren().add(new Label("Status: " + bot.getStatus()));
        vBox.getChildren().add(new Label("Current task: " + bot.getCurrentTask()));
        return vBox;
    }

    private Node addBotButton(){

        Button addBotBotPhysicalButton = new Button("Add robot");


        VBox vBox = new VBox();
        vBox.getChildren().add(addBotBotPhysicalButton);
        return vBox;

    }

    public void addRobot(String name, String com) {
        this.robots.add(new Robot(name, com));
    }

    public void addRobot(String name, int com) {
        this.robots.add(new Robot(name, com));
    }
}
