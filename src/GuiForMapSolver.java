import Enums.Instructions;
import Enums.MapSolverState;
import Enums.WindDirections;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class GuiForMapSolver extends Application {
    private final String mapColorNormal = "-fx-background-color: #b4b4b4";
    private final String mapColorStart = "-fx-background-color: #00ff0b";
    private final String mapColorEnd = "-fx-background-color: #ff0025";
    private final String mapColorSeen = "-fx-background-color: #964487";
    private final String mapColorPath = "-fx-background-color: #3c4296";
    private final String mapColorBlockade = "-fx-background-color: #000000";
    private final String mapColorThrough = "-fx-background-color: #ff6a00";
    private final ArrayList<Instructions> instructions = new ArrayList<>();

    public GuiForMapSolver(Stage stage) {
        try{
            start(stage);
            stage.initModality(Modality.WINDOW_MODAL);
        }catch (Exception e){

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {



        MapSolver mapObject = new MapSolver(MapSolver.makeMap(25, 25));

        BorderPane borderpane = new BorderPane();
        GridPane gridPane = new GridPane();
        HBox hBoxButtons1 = new HBox();
        HBox hBoxButtons2 = new HBox();
        VBox vBoxMenu = new VBox();
       // vBoxMenu.setStyle("-fx-background");

        Button select_back = new Button("Back");
        Button select_run = new Button("Run");
        Button select_reset = new Button("Reset");
        Button select_save = new Button("Save");
        Button select_load = new Button("Load");
        Button select_configure = new Button("Configure");

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
        hBoxButtons2.getChildren().addAll(select_run, select_reset, select_save, select_load,select_configure, select_back);
        hBoxButtons2.setAlignment(Pos.CENTER);
        hBoxButtons2.setSpacing(10);

        vBoxMenu.getChildren().addAll(hBoxButtons1, hBoxButtons2);

        final int[] mapWidthHeight = {25, 25};
        final int[] startingXY = {-1, 0};
        final int[] endingXY = {-1, 0};
        ArrayList<int[]> blockades = new ArrayList<>();
        final int[] throughXY = {-1, 0};


        makeGridPaneMap(mapObject, gridPane, toggleGroup, select_start, startingXY, select_end, endingXY, select_blockade, blockades, select_through, throughXY);

        //TODO
        select_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        //event handlers for Done button
        select_run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //check if the start and end are defined
                if (startingXY[0]==-1||endingXY[0]==-1){
                    System.out.println("no start or end defined");
                    return;
                }

                //check if we need to go through a point
                boolean throughEnablad = false;
                if (throughXY[0] != -1)
                    throughEnablad = true;

                //information to show the user what was selected
                {
                    System.out.println("starting point: (" + startingXY[0] + "," + startingXY[1] + ")");
                    if (throughEnablad)
                        System.out.println("through point: (" + throughXY[0] + "," + throughXY[1] + ")");
                    System.out.println("ending point: (" + endingXY[0] + "," + endingXY[1] + ")");
                    System.out.print("blockades: ");
                    for (int[] b : blockades) {
                        System.out.printf("(%d,%d)", b[0], b[1]);
                    }
                    System.out.println();
                }

                //the enabling of the map solver
                mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0], mapWidthHeight[1]));
                if (!throughEnablad){
                    mapObject.enableMapSolve(startingXY[0], startingXY[1], endingXY[0], endingXY[1]);
                }else {
                    mapObject.enableMapSolve(startingXY[0],startingXY[1],throughXY[0],throughXY[1]);
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
                            if (gridPane.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).getStyle().equals(mapColorNormal))
                                gridPane.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).setStyle(mapColorSeen);
                        }

                        if (mapObject.getState() == MapSolverState.CreatingPath) {
                            if (gridPane.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).getStyle().equals(mapColorSeen))
                                gridPane.getChildren().get(mapObject.getMapWidth() * mapObject.getCurrentY() + mapObject.getCurrentX()).setStyle(mapColorPath);
                        }
                    }

                    //to notify if completed successfully or not
                    {
                        if (mapObject.getState() == MapSolverState.Done) {
                            if (throughEnablad){
                                instructions.addAll(mapObject.getInstructions());
                                WindDirections lastFacingDirection = mapObject.getLastDirection();
                                System.out.println(lastFacingDirection);
                                mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0],mapWidthHeight[1]),lastFacingDirection);
                                mapObject.enableMapSolve(throughXY[0],throughXY[1],endingXY[0],endingXY[1]);
                                throughEnablad = false;
                                for (int[] b : blockades) {
                                    mapObject.makeBlockade(b[0], b[1]);
                                }
                            }else {
                                instructions.addAll(mapObject.getInstructions());
                                System.out.println("route found");
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
            }

        });

        //when the reset button is pressed this will be executed
        select_reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapObject.setMap(MapSolver.makeMap(mapWidthHeight[0], mapWidthHeight[1]));
                gridPane.getChildren().clear();
                blockades.clear();
                startingXY[0] = -1;
                endingXY[0] = -1;
                throughXY[0] = -1;

                makeGridPaneMap(mapObject, gridPane, toggleGroup, select_start, startingXY, select_end, endingXY, select_blockade, blockades, select_through, throughXY);
            }
        });

        //TODO
        select_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        //TODO
        select_load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        //TODO
        select_configure.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                set the map width and height
                */
                mapWidthHeight[0] = 20;
                mapWidthHeight[1] = 20;
            }
        });

        borderpane.setCenter(gridPane);
        borderpane.setBottom(vBoxMenu);
        Scene scene = new Scene(borderpane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map creator");
        primaryStage.show();


    }


    /**
     */

    /**
     * this function makes the map on the screen with buttons
     *
     * @param mapObject       is the map
     * @param gridPane        is the grid pane where the map needs to be displayed using buttons
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
    private void makeGridPaneMap(MapSolver mapObject, GridPane gridPane, ToggleGroup toggleGroup, RadioButton select_start, int[] startingXY, RadioButton select_end, int[] endingXY, RadioButton select_blockade, ArrayList<int[]> blockades, RadioButton select_through, int[] throughXY) {
        for (int y = 0; y < mapObject.getMapHeight(); y++) {
            for (int x = 0; x < mapObject.getMapWidth(); x++) {
                Button button = new Button();
                button.setPrefWidth(25);
                button.setPrefHeight(25);

                button.setStyle(mapColorNormal);

                gridPane.add(button, x, y);
                int buttonXCoordinate = x;
                int buttonYCoordinate = y;


                // logic behind the radio buttons in grid pane buttons
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //logic behind the start button
                        if (toggleGroup.getSelectedToggle().equals(select_start)) {
                            if (endingXY[0] != -1) {

                                //turnoff the old one
                                gridPane.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorNormal);
                                //make sure the end is marked also
                                if (gridPane.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).getStyle().equals(mapColorNormal))
                                    gridPane.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);
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
                                gridPane.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorNormal);
                                //make sure the start is marked also
                                if (gridPane.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPane.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorStart);
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
                                if (!((startingXY[0]==buttonXCoordinate&&startingXY[1]==buttonYCoordinate)||(endingXY[0]==buttonXCoordinate&&endingXY[1]==buttonYCoordinate)||(throughXY[0]==buttonXCoordinate&&throughXY[1]==buttonYCoordinate))){
                                    int[] blockade = {buttonXCoordinate, buttonYCoordinate};
                                    button.setStyle(mapColorBlockade);
                                    blockades.add(blockade);
                                }
                            }
                        } else if (toggleGroup.getSelectedToggle().equals(select_through)) {
                            //turnoff the old one
                            if (throughXY[0] != -1) {
                                gridPane.getChildren().get(mapObject.getMapWidth() * throughXY[1] + throughXY[0]).setStyle(mapColorNormal);
                                //make sure the end is marked as well
                                if (gridPane.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPane.getChildren().get(mapObject.getMapWidth() * endingXY[1] + endingXY[0]).setStyle(mapColorEnd);
                                }
                                //make sure the start is marked as well
                                if (gridPane.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).getStyle().equals(mapColorNormal)) {
                                    gridPane.getChildren().get(mapObject.getMapWidth() * startingXY[1] + startingXY[0]).setStyle(mapColorStart);
                                }
                            }
                            //turn this button on
                            button.setStyle(mapColorThrough);

                            //set the new coordinates
                            throughXY[0] = buttonXCoordinate;
                            throughXY[1] = buttonYCoordinate;
                        }
                    }
                });
            }
        }
    }

//
//    public static void main(String[] args) {
//        //launch(GuiForMapSolver.class);
//    }
}
