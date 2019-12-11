import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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

        Label tunshuName = new Label();

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
