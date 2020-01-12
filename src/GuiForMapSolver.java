import Enums.Instructions;
import Enums.MapSolverState;
import Enums.WindDirections;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


public class GuiForMapSolver  extends Application {
    private final String mapColorNormal = "-fx-background-color: #b4b4b4";
    private final String mapColorStart = "-fx-background-color: #00ff0b";
    private final String mapColorEnd = "-fx-background-color: #ff0025";
    private final String mapColorSeen = "-fx-background-color: #964487";
    private final String mapColorPath = "-fx-background-color: #3c4296";
    private final String mapColorBlockade = "-fx-background-color: #000000";
    private final String mapColorThrough = "-fx-background-color: #ff6a00";

    private ArrayList<Instructions> instructions;
    private RouteCallBack routeCallBack;
    private GridPane gridPaneMap;
    private boolean isDebugEnabled;
    private Stage stage;


    public GuiForMapSolver(Stage stage, RouteCallBack route) {
        try{
            this.stage = stage;
            this.routeCallBack = route;
            start(this.stage);
        }catch (Exception exception){
            if (isDebugEnabled)
                System.out.println(exception);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MapSolver mapObject = new MapSolver(MapSolver.makeMap(25, 25));
        this.instructions = new ArrayList<>();
        BorderPane borderpane = new BorderPane();
        this.gridPaneMap = new GridPane();
        HBox hBoxButtons1 = new HBox();
        HBox hBoxButtons2 = new HBox();
        VBox vBoxMenu = new VBox();

        Button select_run = new Button("Run");
        Button select_reset = new Button("Reset");
        Button select_save = new Button("Save");
        Button select_load = new Button("Load");
        Button select_configure = new Button("Configure");
        Button select_send = new Button(" Send");
        Button select_back = new Button("Back");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton select_start = new RadioButton("Select start");
        RadioButton select_end = new RadioButton("Select end");
        RadioButton select_blockade = new RadioButton("Select blockade");
        RadioButton select_through = new RadioButton("Select through");

        select_start.setToggleGroup(toggleGroup);
        select_end.setToggleGroup(toggleGroup);
        select_blockade.setToggleGroup(toggleGroup);
        select_through.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(select_start);


        hBoxButtons1.getChildren().addAll(select_start, select_end, select_blockade, select_through);
        hBoxButtons1.setAlignment(Pos.CENTER);
        hBoxButtons1.setSpacing(20);
        hBoxButtons1.setPrefHeight(30);
        hBoxButtons2.getChildren().addAll(select_run, select_reset, select_save, select_load, select_configure, select_send,select_back);
        hBoxButtons2.setAlignment(Pos.CENTER);
        hBoxButtons2.setSpacing(10);

        vBoxMenu.getChildren().addAll(hBoxButtons1, hBoxButtons2);

        final int[] mapWidthHeight = {25, 25};
        final int[] startingXY = {-1, 0};
        final int[] endingXY = {-1, 0};
        ArrayList<int[]> blockades = new ArrayList<>();
        final int[] throughXY = {-1, 0};
        this.isDebugEnabled = true;


        makeGridPaneMap(mapObject, toggleGroup, select_start, startingXY, select_end, endingXY, select_blockade, blockades, select_through, throughXY);

        select_back.setOnAction(event -> primaryStage.close());

        select_run.setOnAction(event -> {
            //repaint the map and set clear the instructions array
            instructions.clear();
            repaintMap(startingXY, throughXY, endingXY, blockades, mapWidthHeight);

            //check if the start and end are defined
            if (startingXY[0] == -1 || endingXY[0] == -1) {
                System.out.println("no start or end defined");
                return;
            }

            //check if we need to go through a point
            boolean throughEnablad = false;
            if (throughXY[0] != -1)
                throughEnablad = true;

//            //information to show the user what was selected
//            {
//                if (isDebugEnabled) {
//                    System.out.println("starting point: (" + startingXY[0] + "," + startingXY[1] + ")");
//                    if (throughEnablad) {
//                        System.out.println("through point: (" + throughXY[0] + "," + throughXY[1] + ")");
//                    }
//                    System.out.println("ending point: (" + endingXY[0] + "," + endingXY[1] + ")");
//                    System.out.print("blockades: ");
//                    for (int[] b : blockades) {
//                        System.out.printf("(%d,%d)", b[0], b[1]);
//                    }
//                    System.out.println();
//                }
//            }

            //the enabling of the map solver
            mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0], mapWidthHeight[1]));
            if (!throughEnablad) {
                mapObject.enableMapSolve(startingXY[0], startingXY[1], endingXY[0], endingXY[1]);
            } else {
                mapObject.enableMapSolve(startingXY[0], startingXY[1], throughXY[0], throughXY[1]);
            }
            for (int[] b : blockades) {
                mapObject.makeBlockade(b[0], b[1]);
            }

            //the solve loop
            while (true) {
                //update the solver
                mapObject.update();


                //color the buttons
                {
                    if (mapObject.getState() == MapSolverState.SolvingMap) {
                        if (gridPaneMap.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).getStyle().equals(mapColorNormal))
                            gridPaneMap.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).setStyle(mapColorSeen);
                    }

                    if (mapObject.getState() == MapSolverState.CreatingPath) {
                        if (gridPaneMap.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).getStyle().equals(mapColorSeen))
                            gridPaneMap.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).setStyle(mapColorPath);
                    }
                }

                //to notify if completed successfully or not
                {
                    if (mapObject.getState() == MapSolverState.Done) {
                        if (throughEnablad) {
                            instructions.addAll(mapObject.getInstructions());
                            instructions.add(Instructions.Stop);
                            System.out.println(instructions);
                            WindDirections lastFacingDirection = mapObject.getLastDirection();
                            mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0], mapWidthHeight[1]), lastFacingDirection);
                            mapObject.enableMapSolve(throughXY[0], throughXY[1], endingXY[0], endingXY[1]);
                            throughEnablad = false;
                            for (int[] b : blockades) {
                                mapObject.makeBlockade(b[0], b[1]);
                            }
                        } else {
                            instructions.addAll(mapObject.getInstructions());
                            System.out.println("route found");
                            if (isDebugEnabled)
                                System.out.println(instructions);
                            break;
                        }
                    }
                    if (mapObject.getState() == MapSolverState.Nothing) {
                        System.out.println("no valid route found");
                        break;
                    }
                }
            }
        });

        //when the reset button is pressed this will be executed
        select_reset.setOnAction(event -> {
            mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0], mapWidthHeight[1]));
            gridPaneMap.getChildren().clear();
            blockades.clear();
            startingXY[0] = -1;
            endingXY[0] = -1;
            throughXY[0] = -1;

            makeGridPaneMap(mapObject, toggleGroup, select_start, startingXY, select_end, endingXY, select_blockade, blockades, select_through, throughXY);
            primaryStage.sizeToScene();
        });

        select_save.setOnAction(event -> {
            FlowPane saveWindow = new FlowPane();
            TextField textField = new TextField();
            Label label = new Label("Name:");
            Button saveButton = new Button("Save");
            Button cancelButton = new Button("Cancel");
            HBox hBoxText = new HBox(label, textField);
            HBox hBoxButtons = new HBox(saveButton, cancelButton);
            saveWindow.getChildren().addAll(hBoxText, hBoxButtons);
            Scene scene = new Scene(saveWindow);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Save");
            stage.show();
            StringBuilder name = new StringBuilder();

            cancelButton.setOnAction(close -> stage.close());

            saveButton.setOnAction(save -> {
                if (textField.getText().isEmpty()) {
                    label.setText("Must input name!");
                } else {

                    name.append(textField.getText());
                    File file = new File("./Routes/" + name.toString());
                    try {
                        //if the routes.txt file doesn't exist make a new text file
                        if (!file.exists())
                            file.createNewFile();

                        FileWriter fileWriter = new FileWriter(file);
                        //place where the route is stored
                        StringBuilder saveFile = new StringBuilder();

                        //name of the route
                        saveFile.append("Name:").append(name).append("\n");

                        //save map width and height
                        saveFile.append("Width:").append(mapWidthHeight[0]).append("\n");
                        saveFile.append("Height:").append(mapWidthHeight[1]).append("\n");

                        //if starting point is filled in
                        if (startingXY[0] != -1) {
                            String start = "StartingPoint(" + startingXY[0] + "," + startingXY[1] + ")\n";
                            saveFile.append(start);
                        }

                        //if through point is filled in
                        if (throughXY[0] != -1) {
                            String through = "ThroughPoint(" + throughXY[0] + "," + throughXY[1] + ")\n";
                            saveFile.append(through);
                        }

                        //if end point is filled in
                        if (endingXY[0] != -1) {
                            String end = "EndPoint(" + endingXY[0] + "," + endingXY[1] + ")\n";
                            saveFile.append(end);
                        }

                        //if there are any blockades
                        if (!blockades.isEmpty()) {
                            for (int[] b : blockades) {
                                String blockade = "Blockade(" + b[0] + "," + b[1] + ")\n";
                                saveFile.append(blockade);
                            }
                        }

                        //write to the file
                        fileWriter.write(saveFile.toString());

                        fileWriter.close();
                        stage.close();
                    } catch (Exception exception) {
                        System.out.println("Exception in saving: "+exception);
                    }
                }
            });

        });

        select_load.setOnAction(loadRoutes -> {
            try {
                Collection<File> routes = new ArrayList<>();
                File file = new File("./Routes");
                File[] children = file.listFiles();
                if (children != null) {
                    routes.addAll(Arrays.asList(children));
                }
                ComboBox comboBox = new ComboBox();
                comboBox.getItems().addAll(routes);
                try {
                    comboBox.setValue(comboBox.getItems().get(0));
                } catch (Exception exception) {
                    if (isDebugEnabled)
                        System.out.println("in load startup:"+exception);
                }

                Stage stage_load = new Stage();
                Button button_load = new Button("Load");
                Button button_delete = new Button("Delete");
                Button button_back = new Button("Back");

                button_load.setOnAction(load -> {
                    File route = (File) comboBox.getValue();
                    try {
                        Scanner scanner = new Scanner(route);
                        String searchCriteriaName = "Name:";
                        String searchCriteriaWidth = "Width:";
                        String searchCriteriaHeight = "Height:";
                        String searchCriteriaStart = "StartingPoint(";
                        String searchCriteriaThrough = "ThroughPoint(";
                        String searchCriteriaEnd = "EndPoint(";
                        String searchCriteriaBlockade = "Blockade(";

                        //while loop to go through every line of the file
                        while (scanner.hasNext()) {
                            String line = scanner.next();

                            //load name
                            if (line.contains(searchCriteriaName)) {
                                System.out.println("loaded route: "+line.substring(searchCriteriaName.length()));


                                //load width of saved map
                            } else if (line.contains(searchCriteriaWidth)) {
                                int mapWidth = Integer.parseInt(line.substring(searchCriteriaWidth.length()));
                                if (mapWidth > 1 && mapWidth < 50) {
                                    mapWidthHeight[0] = mapWidth;
                                }


                                //load Height of map
                            } else if (line.contains(searchCriteriaHeight)) {
                                int mapHeight = Integer.parseInt(line.substring(searchCriteriaHeight.length()));
                                if (mapHeight > 1 && mapHeight < 50) {
                                    mapWidthHeight[1] = mapHeight;
                                }
                                //make the map with the given width and height
                                select_reset.fire();


                                //loading start point
                            } else if (line.contains(searchCriteriaStart)) {
                                String coordinates = line.substring(searchCriteriaStart.length(), line.length()-1);

                                //find the separator of the two coordinates
                                int counter = 0;
                                while (coordinates.charAt(counter) != ',') {
                                    counter++;
                                }

                                //get the two coordinates out of the line
                                int x = Integer.parseInt(coordinates.substring(0, counter));
                                int y = Integer.parseInt(coordinates.substring(counter+1));
                                startingXY[0] = x;
                                startingXY[1] = y;


                                //color the start button the starting color
                                gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorStart);


                                //loading through point
                            } else if (line.contains(searchCriteriaThrough)) {
                                String coordinates = line.substring(searchCriteriaThrough.length(), line.length() - 1);
                                int counter = 0;
                                while (coordinates.charAt(counter) != ',') {
                                    counter++;
                                }

                                //get the two coordinates out of the line
                                int x = Integer.parseInt(coordinates.substring(0, counter));
                                int y = Integer.parseInt(coordinates.substring(counter+1));
                                throughXY[0] = x;
                                throughXY[1] = y;

                                //color the through button
                                gridPaneMap.getChildren().get(mapObject.getMapWidth() * throughXY[1] + throughXY[0]).setStyle(mapColorThrough);


                                //loading endpoint
                            } else if (line.contains(searchCriteriaEnd)) {
                                String coordinates = line.substring(searchCriteriaEnd.length(), line.length() - 1);
                                int counter = 0;
                                while (coordinates.charAt(counter) != ',') {
                                    counter++;
                                }

                                //get the two coordinates out of the line
                                int x = Integer.parseInt(coordinates.substring(0, counter));
                                int y = Integer.parseInt(coordinates.substring(counter+1));
                                endingXY[0] = x;
                                endingXY[1] = y;

                                //color the end button
                                gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);


                            } else if (line.contains(searchCriteriaBlockade)) {
                                String coordinates = line.substring(searchCriteriaBlockade.length(),line.length()-1);
                                int counter = 0;
                                while (coordinates.charAt(counter) != ',') {
                                    counter++;
                                }


                                //get the two coordinates out of the line
                                int x = Integer.parseInt(coordinates.substring(0, counter));
                                int y = Integer.parseInt(coordinates.substring(counter+1));

                                int[] blockade = {x, y};
                                blockades.add(blockade);
                                gridPaneMap.getChildren().get(mapObject.getMapWidth() * y + x).setStyle(mapColorBlockade);

                            } else {
                                if (isDebugEnabled) {
                                    System.out.println("Line not recognised: "+line);
                                }
                            }
                        }
                        select_run.fire();
                    } catch (Exception exception) {
                        if (isDebugEnabled)
                            System.out.println("In Load:"+exception);

                    }
                });

                button_delete.setOnAction(delete -> {
                    File deletableFile = (File) comboBox.getValue();
                    if (isDebugEnabled) {
                        System.out.println(deletableFile.delete());
                    } else {
                        deletableFile.delete();
                    }
                });


                button_back.setOnAction(back -> stage_load.close());

                FlowPane flowPane_load = new FlowPane();
                flowPane_load.getChildren().addAll(comboBox, button_load, button_delete, button_back);
                Scene scene_load = new Scene(flowPane_load);
                stage_load.setScene(scene_load);
                stage_load.show();

            } catch (Exception exception) {
                if (isDebugEnabled)
                    System.out.println("Exception in making load screen: "+exception);
            }
        });


        select_configure.setOnAction(configure -> {
            Stage stage = new Stage();
            FlowPane flowPane = new FlowPane();
            Label label_mapWidth = new Label("Map width:");
            TextField textField_mapWidth = new TextField(""+mapWidthHeight[0]);
            Label label_mapHeight = new Label("Map height:");
            TextField textField_mapHeight = new TextField(""+mapWidthHeight[1]);
            Button button_apply = new Button("Apply");
            Button button_reset = new Button("Reset");
            Button button_back = new Button("Back");
            HBox hBox_buttons = new HBox(button_apply, button_reset, button_back);
            CheckBox checkBox = new CheckBox("Debugger");
            checkBox.setSelected(isDebugEnabled);

            flowPane.getChildren().addAll(label_mapWidth, textField_mapWidth, label_mapHeight, textField_mapHeight, checkBox, hBox_buttons);
            flowPane.setPrefWidth(200);
            button_apply.setOnAction(event1 -> {
                try {
                    if (!textField_mapWidth.getText().isEmpty())
                        if (Integer.parseInt(textField_mapWidth.getText()) > 1 && Integer.parseInt(textField_mapWidth.getText()) < 50) {
                            mapWidthHeight[0] = Integer.parseInt(textField_mapWidth.getText());
                        } else {
                            if (isDebugEnabled)
                                System.out.println("no valid map width selected");
                        }
                } catch (NumberFormatException exception) {
                    if (isDebugEnabled)
                        System.out.println("Exception in getting map width: "+exception);
                }

                try {
                    if (!textField_mapHeight.getText().isEmpty())
                        if (Integer.parseInt(textField_mapHeight.getText()) > 1 && Integer.parseInt(textField_mapHeight.getText()) < 31) {
                            mapWidthHeight[1] = Integer.parseInt(textField_mapHeight.getText());
                        } else {
                            if (isDebugEnabled)
                                System.out.println("no valid map height selected");
                        }
                } catch (NumberFormatException exception) {
                    if (isDebugEnabled)
                        System.out.println("Exception in getting map height: "+exception);
                }


                isDebugEnabled = checkBox.isSelected();
                select_reset.fire();
            });

            button_back.setOnAction(close -> stage.close());

            button_reset.setOnAction(reset -> {
                mapWidthHeight[0] = 25;
                mapWidthHeight[1] = 25;
                checkBox.setSelected(false);
                button_apply.fire();
            });

            Scene scene = new Scene(flowPane);
            stage.setScene(scene);
            stage.setTitle("Configure");
            stage.show();
        });

        select_send.setOnAction(event -> {
            String route = "";
            for (Instructions instruction : this.instructions) {
                switch (instruction){
                    case Forward:
                        route += "w";
                        break;
                    case Left:
                        route += "a";
                        break;
                    case Right:
                        route += "d";
                        break;
                    case Stop:
                        route += "s";
                        break;
                }
            }
           this.routeCallBack.sendRoute(route);
        });

        borderpane.setCenter(gridPaneMap);
        borderpane.setBottom(vBoxMenu);
        Scene scene = new Scene(borderpane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map creator");
        primaryStage.show();


    }


    /**
     * this function makes the map on the screen with buttons
     *
     * @param mapObject       is the map
     * @param toggleGroup     the radio buttons are stored in here
     * @param select_start    the radio buttons select start
     * @param startingXY      the starting coordinates where x is index 0 and y is index 1
     * @param select_end      is the radio button select end
     * @param endingXY        the ending coordinates where x is index 0 and y is index 1
     * @param select_blockade is the radio buttons select blockade
     * @param blockades       is an array list of coordinate arrays like startingXY and endingXY where index 0 is the x and index 1 is the y
     * @param select_through  ia the radio button select through
     * @param throughXY       the through coordinates where x is index 0 and y is index 1
     */
    private void makeGridPaneMap(MapSolver mapObject, ToggleGroup toggleGroup, RadioButton select_start, int[] startingXY, RadioButton select_end, int[] endingXY, RadioButton select_blockade, ArrayList<int[]> blockades, RadioButton select_through, int[] throughXY) {
        for (int y = 0; y < mapObject.getMapHeight(); y++) {
            for (int x = 0; x < mapObject.getMapWidth(); x++) {
                Button button = new Button();
                button.setPrefWidth(25);
                button.setPrefHeight(25);

                button.setStyle(mapColorNormal);

                gridPaneMap.add(button, x, y);
                int buttonXCoordinate = x;
                int buttonYCoordinate = y;


                // logic behind the radio buttons in grid pane buttons
                button.setOnAction(event -> {
                    //logic behind the start button
                    if (toggleGroup.getSelectedToggle().equals(select_start)) {
                        if (startingXY[0] != -1) {

                            //turnoff the old one
                            this.gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorNormal);

                            //make sure the end is marked also
                            if (endingXY[0] != -1)
                                if (this.gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).getStyle().equals(mapColorNormal))
                                    this.gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);
                        }
                        //turn this button on
                        button.setStyle(mapColorStart);

                        //set the new coordinates
                        startingXY[0] = buttonXCoordinate;
                        startingXY[1] = buttonYCoordinate;

                        //logic behind the end button
                    } else if (toggleGroup.getSelectedToggle().equals(select_end)) {
                        if (endingXY[0] != -1) {
                            //turnoff the old one
                            gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorNormal);
                            //make sure the start is marked also
                            if (startingXY[0] != -1)
                                if (gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorStart);
                                }
                        }
                        //turn this button on
                        button.setStyle(mapColorEnd);

                        //set the new coordinates
                        endingXY[0] = buttonXCoordinate;
                        endingXY[1] = buttonYCoordinate;


                        //logic behind the blockade button
                    } else if (toggleGroup.getSelectedToggle().equals(select_blockade)) {
                        boolean wasBlocked = false;
                        int[] toBeRemoved = {0, 0};
                        //check if that square was already blocked
                        for (int[] b : blockades) {
                            if (b[0] == buttonXCoordinate && b[1] == buttonYCoordinate) {
                                toBeRemoved = b;
                                button.setStyle(mapColorNormal);
                                wasBlocked = true;
                            }
                        }
                        //unblocking mechanism
                        if (wasBlocked) {
                            blockades.remove(toBeRemoved);
                        } else {
                            //check if it isn't the start, end or through point
                            if (!((startingXY[0] == buttonXCoordinate && startingXY[1] == buttonYCoordinate) || (endingXY[0] == buttonXCoordinate && endingXY[1] == buttonYCoordinate) || (throughXY[0] == buttonXCoordinate && throughXY[1] == buttonYCoordinate))) {
                                int[] blockade = {buttonXCoordinate, buttonYCoordinate};
                                button.setStyle(mapColorBlockade);
                                blockades.add(blockade);
                            }
                        }
                    } else if (toggleGroup.getSelectedToggle().equals(select_through)) {
                        //turnoff the old one
                        if (throughXY[0] != -1) {
                            gridPaneMap.getChildren().get(mapObject.getMapWidth() * throughXY[1] + throughXY[0]).setStyle(mapColorNormal);
                            //make sure the end is marked as well
                            if (endingXY[0] != -1)
                                if (gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPaneMap.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);
                                }
                            //make sure the start is marked as well
                            if (startingXY[0] != -1)
                                if (gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPaneMap.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorStart);
                                }
                        }
                        //turn this button on
                        button.setStyle(mapColorThrough);

                        //set the new coordinates
                        throughXY[0] = buttonXCoordinate;
                        throughXY[1] = buttonYCoordinate;
                    }
                });
            }
        }
    }

    /**
     * this function is painting every button normal and then paints the given coordinates the according color
     *
     * @param startingXY     the starting coordinates where x is index 0 and y is index 1
     * @param throughXY      the through coordinates where x is index 0 and y is index 1
     * @param endingXY       the ending coordinates where x is index 0 and y is index 1
     * @param blockades      is an array list of coordinate arrays like startingXY and endingXY where index 0 is the x and index 1 is the y
     * @param mapWidthHeight the dimensions of the map where width is index 0 and height is index 1
     */
    private void repaintMap(int[] startingXY, int[] throughXY, int[] endingXY, ArrayList<int[]> blockades, int[] mapWidthHeight) {
        for (int y = 0; y < mapWidthHeight[1]; y++) {
            for (int x = 0; x < mapWidthHeight[0]; x++) {
                gridPaneMap.getChildren().get(mapWidthHeight[1] * y + x).setStyle(mapColorNormal);
            }
        }
        //make sure the end is marked as well
        if (endingXY[0] != -1)
            if (gridPaneMap.getChildren().get(mapWidthHeight[1] * endingXY[1] + endingXY[0]).getStyle().equals(mapColorNormal)) {
                gridPaneMap.getChildren().get(mapWidthHeight[1] * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);
            }
        //make sure the start is marked as well
        if (startingXY[0] != -1)
            if (gridPaneMap.getChildren().get(mapWidthHeight[1] * startingXY[1] + startingXY[0]).getStyle().equals(mapColorNormal)) {
                gridPaneMap.getChildren().get(mapWidthHeight[1] * startingXY[1] + startingXY[0]).setStyle(mapColorStart);
            }
        //make sure the through is marked as well
        if (throughXY[0] != -1)
            if (gridPaneMap.getChildren().get(mapWidthHeight[1] * throughXY[1] + throughXY[0]).getStyle().equals(mapColorNormal)) {
                gridPaneMap.getChildren().get(mapWidthHeight[1] * throughXY[1] + throughXY[0]).setStyle(mapColorThrough);
            }

        //make sure the blockades are drawn
        for (int[] b : blockades) {
            if (gridPaneMap.getChildren().get(mapWidthHeight[1] * b[1] + b[0]).getStyle().equals(mapColorNormal)) {
                gridPaneMap.getChildren().get(mapWidthHeight[1] * b[1] + b[0]).setStyle(mapColorBlockade);
            }
        }
    }
}
