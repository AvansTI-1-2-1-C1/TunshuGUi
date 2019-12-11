import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application{

    public static void main(String[] args) {

        launch(Main.class);

    }

    public void start( Stage stage ){

        //Window characteristics: size/name
        stage.setHeight(700);
        stage.setWidth(900);
        stage.setResizable(false);
        stage.setTitle("Tunshu console");

        //Application main layout
        BorderPane mainWindowLayout = new BorderPane();

        Scene mainView = new Scene(mainWindowLayout);

        mainWindowLayout.setTop( topBar() );
        mainWindowLayout.setCenter( botControl() );

        stage.setScene(mainView);

        stage.show();

    }

    private Node botControl() {

        BorderPane centerControlLayout = new BorderPane();

        Label tunshuName = new Label("TEST TITLE");
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

        driveForward.setMinSize(100.0,100.0);
        driveLeft.setMinSize(100.0,100.0);
        driveRight.setMinSize(100.0,100.0);
        driveBack.setMinSize(100.0,100.0);
        brake.setMinSize(320.0, 100.0);

        buttonControlLayout.add(driveForward,1, 0);
        buttonControlLayout.add(driveLeft, 0, 1);
        buttonControlLayout.add(driveRight, 2, 1);
        buttonControlLayout.add(driveBack, 1, 1);
        buttonControlLayout.add(brake, 0,2,3,1);


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

}
