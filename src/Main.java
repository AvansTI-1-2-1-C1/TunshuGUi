import Enums.DriveCommands;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application implements RouteCallBack{

    private GuiLogic guiLogic = new GuiLogic();
    private Label botName;
    private Label stateName;
    private Label portName;


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


        //Application main layout
        BorderPane mainWindowLayout = new BorderPane();

        Scene mainView = new Scene(mainWindowLayout);

        mainView.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getCode()) {
                case W:
                    guiLogic.button(DriveCommands.Forward);
                    break;
                case A:
                    guiLogic.button(DriveCommands.Left);
                    break;
                case D:
                    guiLogic.button(DriveCommands.Right);
                    break;
                case S:
                    guiLogic.button(DriveCommands.Backward);
                    break;
                case SPACE:
                    guiLogic.button(DriveCommands.Brake);
                    break;
                case H:
                    guiLogic.button(DriveCommands.Handbrake);
                    break;
                case M:
                    guiLogic.button(DriveCommands.Mute);
                    break;
                case L:
                    guiLogic.button(DriveCommands.LineFollower);
                    break;
                case ESCAPE:
                    Platform.exit();
                    System.exit(0);
                    break;
                default:
                    System.out.println(key.getCode().getName());
                    break;
            }
        });

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
        centerControlLayout.setMinSize(514, 560);
        centerControlLayout.setMaxSize(514, 560);
        centerControlLayout.setStyle("-fx-background-color: #121212;");

        HBox insideCenterControlLayoutUp = new HBox();
        insideCenterControlLayoutUp.setMinSize(484, 242);
        insideCenterControlLayoutUp.setMaxSize(484, 242);
        VBox.setMargin(insideCenterControlLayoutUp, new Insets(18, 0, 0, 10));

        VBox informationContainer = new VBox();
        informationContainer.setMinSize(230, 242);
        informationContainer.setMaxSize(230, 242);
        informationContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        informationContainer.setAlignment(Pos.TOP_CENTER);

        VBox containerForInformationTruly = new VBox();
        containerForInformationTruly.setMinSize(230, 180);
        containerForInformationTruly.setMaxSize(230, 180);
        containerForInformationTruly.setAlignment(Pos.TOP_LEFT);

        Label informationTitle = new Label("Information");
        informationTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        informationTitle.setTextFill(Color.WHITE);
        informationTitle.setPadding(new Insets(5, 0, 5, 0));

        botName = new Label("Name: " + this.guiLogic.getSelected().getName());
        botName.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        botName.setTextFill(Color.WHITE);
        botName.setPadding(new Insets(10, 0, 5, 20));

        stateName = new Label("State: " + this.guiLogic.getSelected().getStatus());
        stateName.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        stateName.setTextFill(Color.WHITE);
        stateName.setPadding(new Insets(10, 0, 5, 20));

        portName = new Label("Port: " + this.guiLogic.getSelected().getPort());
        portName.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        portName.setTextFill(Color.WHITE);
        portName.setPadding(new Insets(10, 0, 5, 20));

        Label batteryStateName = new Label("Battery: --");
        batteryStateName.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        batteryStateName.setTextFill(Color.WHITE);
        batteryStateName.setPadding(new Insets(10, 0, 5, 20));

        containerForInformationTruly.getChildren().addAll(botName,stateName,portName,batteryStateName);
        informationContainer.getChildren().addAll(informationTitle,containerForInformationTruly);


        VBox botFunNavContainer = new VBox();
        botFunNavContainer.setMinSize(236, 242);
        botFunNavContainer.setMaxSize(236, 242);
        HBox.setMargin(botFunNavContainer, new Insets(0, 0, 0, 18));

        VBox botFunctionsContainer = new VBox();
        botFunctionsContainer.setMinSize(236, 112);
        botFunctionsContainer.setMaxSize(236, 112);
        botFunctionsContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        //VBox.setMargin( botFunctionsContainer, new Insets(0,0,0,0));
        botFunctionsContainer.setAlignment(Pos.TOP_CENTER);

        Label functionsTitle = new Label("Functions");
        functionsTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        functionsTitle.setTextFill(Color.WHITE);
        functionsTitle.setPadding(new Insets(5, 0, 5, 0));

        botFunctionsContainer.getChildren().add(functionsTitle);
        //botFunctionsContainer.getChildren().add(onOffLights());
        botFunctionsContainer.getChildren().add(muteUnMuteSound());
        botFunctionsContainer.getChildren().add(speedSlider());

        VBox botNavContainer = new VBox();
        botNavContainer.setMinSize(236, 112);
        botNavContainer.setMaxSize(236, 112);
        botNavContainer.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        VBox.setMargin(botNavContainer, new Insets(18, 0, 0, 0));
        botNavContainer.setAlignment(Pos.TOP_CENTER);

        Label navigationTitle = new Label("Navigation");
        navigationTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        navigationTitle.setTextFill(Color.WHITE);
        navigationTitle.setPadding(new Insets(5, 0, 20, 0));


// LINE FOLLOWER BUTTON CREATION & SIZING
        Button lineFollower = new Button("Drive route");
        lineFollower.setMinSize(140,35);
        lineFollower.setMaxSize(140,35);
        lineFollower.setStyle("-fx-background-color: #1F1826");

        Stage mapSolverStage = new Stage();

        lineFollower.setOnAction(e -> {
            if(!mapSolverStage.isShowing()) {
                GuiForMapSolver guiForMapSolver = new GuiForMapSolver(mapSolverStage, this);
            }else {
                mapSolverStage.toFront();
            }
        });

        lineFollower.setOnMousePressed(e -> lineFollower.setStyle("-fx-background-color: White; "));
        lineFollower.setOnMouseReleased(e -> lineFollower.setStyle("-fx-background-color: #1F1826;"));

        botNavContainer.getChildren().addAll(navigationTitle, lineFollower);

        HBox insideCenterControlLayoutDown = new HBox();
        insideCenterControlLayoutDown.setMinSize(484, 242);
        insideCenterControlLayoutDown.setMaxSize(484, 242);
        insideCenterControlLayoutDown.setStyle("-fx-background-radius: 10; -fx-background-color: #28202F;");
        VBox.setMargin(insideCenterControlLayoutDown, new Insets(18, 0, 0, 10));
        insideCenterControlLayoutDown.setAlignment(Pos.TOP_CENTER);

        Label controlTitle = new Label("Manual Control");
        controlTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        controlTitle.setTextFill(Color.WHITE);
        controlTitle.setPadding(new Insets(10, 0, 5, 0));

        VBox bottomControlContainer = new VBox();
        bottomControlContainer.setAlignment(Pos.TOP_CENTER);

        bottomControlContainer.getChildren().add(controlTitle);

        botFunNavContainer.getChildren().addAll(botFunctionsContainer, botNavContainer);

        insideCenterControlLayoutUp.getChildren().addAll(informationContainer, botFunNavContainer);
        insideCenterControlLayoutDown.getChildren().addAll(bottomControlContainer);

        centerControlLayout.getChildren().addAll(insideCenterControlLayoutUp, insideCenterControlLayoutDown);

        Label tunshuName = new Label(this.guiLogic.getSelected().getName());
        tunshuName.setStyle("-fx-font-size: 42");
        tunshuName.setTextFill(Color.WHITE);


// GRIDPANE HOLDING CONTROL BUTTONS CREATION
        GridPane buttonControlLayout = new GridPane();


// CONTROL BUTTON CREATION
        Button driveForward = new Button("W");
        Button driveLeft = new Button("A");
        Button driveRight = new Button("D");
        Button driveBack = new Button("S");
        Button brake = new Button("⎵");
        Button emergencyBrake = new Button();
        Button mute = new Button();


// CONTROL BUTTON ACTION WHEN CLICKED
        // Control button event handlers send commands through to the GuiLogic Class when clicked.
        // There to be further sent to the robot to control the robots  behaviour.
        driveForward.setOnAction(event -> {
            guiLogic.button(DriveCommands.Forward);
        } );
        driveLeft.setOnAction(event -> {
            guiLogic.button(DriveCommands.Left);
        });
        driveRight.setOnAction(event -> {
            guiLogic.button(DriveCommands.Right);
        });
        driveBack.setOnAction(event -> {
            guiLogic.button(DriveCommands.Backward);
        });
        brake.setOnAction(event -> {
            guiLogic.button(DriveCommands.Brake);
        });
        emergencyBrake.setOnAction(event -> {
            guiLogic.button(DriveCommands.Handbrake);
        });
        mute.setOnAction(event -> {
            guiLogic.button(DriveCommands.Mute);
        } );
//        lineFollower.setOnAction(event -> {
//            guiLogic.button(Enums.DriveCommands.LineFollower);
//        } );



        buttonControlLayout.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                driveForward.setStyle("-fx-background-color: White; ");
            }
        });
//        driveForward.setOnKeyReleased(e -> {
//            if (e.getCode() == KeyCode.W) {
//                driveForward.setStyle("-fx-background-color: #1F1826;");
//            }
//        });


// CONTROL BUTTON BASE COLOR SET
        driveForward.setStyle("-fx-background-color: #1F1826");
        driveLeft.setStyle("-fx-background-color: #1F1826");
        driveRight.setStyle("-fx-background-color: #1F1826");
        driveBack.setStyle("-fx-background-color: #1F1826");
        brake.setStyle("-fx-background-color: #1F1826");
        emergencyBrake.setStyle("-fx-background-color: #DF1D1D");
        mute.setStyle("-fx-background-color: black");


// CONTROL BUTTON COLOR ON MOUSE EVENT
        // Control buttons event handlers that change the color of the button when mouse is over the button in question.
        // Buttons are:  Forward = W
        //              Left = D
        //              Right = A
        //              Back = S
        //              Break = SpaceBar
        driveForward.setOnMousePressed(e -> driveForward.setStyle("-fx-background-color: White; "));
        driveForward.setOnMouseReleased(e -> driveForward.setStyle("-fx-background-color: #1F1826;"));

        driveLeft.setOnMousePressed(e -> driveLeft.setStyle("-fx-background-color: White; "));
        driveLeft.setOnMouseReleased(e -> driveLeft.setStyle("-fx-background-color: #1F1826;"));

        driveRight.setOnMousePressed(e -> driveRight.setStyle("-fx-background-color: White; "));
        driveRight.setOnMouseReleased(e -> driveRight.setStyle("-fx-background-color: #1F1826;"));

        driveBack.setOnMousePressed(e -> driveBack.setStyle("-fx-background-color: White; "));
        driveBack.setOnMouseReleased(e -> driveBack.setStyle("-fx-background-color: #1F1826;"));

        brake.setOnMousePressed(e -> brake.setStyle("-fx-background-color: White; "));
        brake.setOnMouseReleased(e -> brake.setStyle("-fx-background-color: #1F1826;"));


// CONTROL BUTTONs LAYOUT SPACING SET
        buttonControlLayout.setHgap(10.0);
        buttonControlLayout.setVgap(10.0);


// CONTROL BUTTONs SIZE SET
        driveForward.setMinSize(60.0, 60.0);
        driveLeft.setMinSize(60.0, 60.0);
        driveRight.setMinSize(60.0, 60.0);
        driveBack.setMinSize(60.0, 60.0);
        brake.setMinSize(126, 60.0);
        brake.setMaxSize(126, 60.0);

        emergencyBrake.setMinSize(70.0, 70.0);
        mute.setMinSize(70.0, 70.0);


// CONTROL BUTTONs ADDED TO BUTTON LAYOUT GRID
        buttonControlLayout.add(driveForward, 1, 0);
        buttonControlLayout.add(driveLeft, 0, 1);
        buttonControlLayout.add(driveRight, 2, 1);
        buttonControlLayout.add(driveBack, 1, 1);
        buttonControlLayout.add(brake, 4, 1, 3, 1);
//        buttonControlLayout.add(emergencyBrake,0,8);

        buttonControlLayout.setHalignment(emergencyBrake, HPos.CENTER);
        //buttonControlLayout.setHalignment(lineFollower, HPos.CENTER);
        buttonControlLayout.setHalignment(mute, HPos.CENTER);

        buttonControlLayout.setPadding(new Insets(35, 0, 0, 0));

        bottomControlContainer.getChildren().add(buttonControlLayout);

        return centerControlLayout;
    }

    /**
     * GUI NODE FOR THE ROBOT SPEED SLIDER
     *
     * Slider is created.
     * Slider sends values through to the GuiLogic class to be further sent to the robot, changing its driving speed.
     *
     * @return
     */
    private Node speedSlider() {

        HBox speedSliderContainer = new HBox();
        speedSliderContainer.setPadding(new Insets(0, 0, 0, 15));

        Label speedSliderTitle = new Label("Speed");
        speedSliderTitle.setTextFill(Color.WHITE);
        speedSliderTitle.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica';");
        speedSliderTitle.setPadding(new Insets(3, 0, 0, 2));

        Slider speedSlider = new Slider(0.0f, 1f, 0.4);
        speedSlider.setBlockIncrement(0.2);//WHY TF WONT YOU WORK?
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);

        speedSlider.setPadding(new Insets(3, 0, 0, 5));

        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                guiLogic.speedSetting(newValue);
                System.out.println(newValue);
            }
        });

        speedSliderContainer.getChildren().addAll(speedSliderTitle, speedSlider);

        return speedSliderContainer;
    }

    /**
     * SOUND AND LIGHT TOGGLES
     *
     * Both toggles are created.
     * Radio buttons send their value through to the GuiLogic class to be further sent to the robot, changing the state of its sound and/or light.
     *
     * @return
     */
    private Node muteUnMuteSound() {

        VBox soundMuteContainer = new VBox();
        soundMuteContainer.setPadding(new Insets(0, 0, 0, 15));

//        Label soundMuteTitle = new Label("Sound");
//        soundMuteTitle.setTextFill(Color.WHITE);
//        soundMuteTitle.setStyle("-fx-font-size: 12; -fx-font-family: 'Helvetica';");
        ToggleGroup soundSetting = new ToggleGroup();
        ToggleGroup lightsSetting = new ToggleGroup();
        RadioButton muteUnMuteButton = new RadioButton("Sound");
        RadioButton onOffLightsButton = new RadioButton("Lights ");

        muteUnMuteButton.setToggleGroup(soundSetting);
        onOffLightsButton.setToggleGroup(lightsSetting);

        soundMuteContainer.getChildren().addAll(muteUnMuteButton, onOffLightsButton);


        return soundMuteContainer;
    }

    private Node topBar() {

        HBox topBar = new HBox();
        topBar.setMinHeight(40.0);

        Label consoleTitle = new Label("Tunshu Console");
        consoleTitle.setTextFill(Color.WHITE);
        consoleTitle.setStyle("-fx-font-size: 20; -fx-font-family: 'Helvetica';");
        consoleTitle.setPadding(new Insets(0, 0, 0, 15));

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

        for (Robot bot : this.guiLogic.getRobots()) {
            if (!bot.getName().equals("No robot selected")) {
                listView.getItems().add(botGUI(bot));
            }
        }

        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue) -> {
            this.guiLogic.setSelected(1+listView.getSelectionModel().getSelectedIndex());
            botName.setText("Name: " + this.guiLogic.getSelected().getName());
            stateName.setText("State: " + this.guiLogic.getSelected().getStatus());
            portName.setText("Port: " + this.guiLogic.getSelected().getPort());
        });


        Label botListTitle = new Label("Bot List");
        botListTitle.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica';");
        botListTitle.setTextFill(Color.WHITE);
        botListTitle.setPadding(new Insets(5, 0, 5, 0));

        VBox vBox = new VBox();
        vBox.getChildren().add(botListTitle);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().add(listView);

        vBox.setMinHeight(502.0);
        vBox.setMinWidth(263.0);
        vBox.setMaxSize(263.0, 522.0);
        VBox.setMargin(vBox, new Insets(18, 0, 0, 15));
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

//    private Node addBotButton(){
//
//        Button addBotBotPhysicalButton = new Button("Add robot");
//
//
//        VBox vBox = new VBox();
//        vBox.getChildren().add(addBotBotPhysicalButton);
//        return vBox;
//
//    }

    public void addRobot(String name, String com) {
        this.guiLogic.addRobot(name, com);
    }

    @Override
    public void sendRoute(String route) {
        this.guiLogic.getSelected().send("");
    }
}
