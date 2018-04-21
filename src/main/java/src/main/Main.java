package src.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        primaryStage.setTitle("SBD");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {   //zakoncz wszystkie watki jak zamyka sie aplkacje Xem
                Platform.exit();
                System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}
