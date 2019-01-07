import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String resourcePath = "view/main.fxml";
        URL mainViewLocation = getClass().getResource(resourcePath);
        Parent root = FXMLLoader.load(mainViewLocation);

        primaryStage.setTitle("Real-Time Scheduler Simulator");
        primaryStage.setScene(new Scene(root, 900, 540));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
