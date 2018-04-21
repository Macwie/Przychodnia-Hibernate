package src.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import src.main.CreateSession;
import src.main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {


    @FXML
    public Pane progress;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button manageButton;

    @FXML
    private Button termButton;

    @FXML
    private ImageView img;


    //****Methods
    @FXML
    void openManageView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ManageView.fxml"));
        AnchorPane pane = loader.load();
        ManageViewController manageViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void openVisitView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/VisitView.fxml"));
        AnchorPane pane = loader.load();
        VisitViewController visitViewController = loader.getController();
        rootPane.getChildren().setAll(pane);
        //wywolujemy metode z nowego okna i przekazujemy dane
        ///addPatientViewController.setText(testTextField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        final CreateSession create = new CreateSession();
        progress.visibleProperty().bind(create.runningProperty());
        create.setOnSucceeded(workerStateEvent -> {
            create.getValue();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), manageButton);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.95);
            fadeIn.play();
            FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(1.0), termButton);
            fadeIn2.setFromValue(0.0);
            fadeIn2.setToValue(0.95);
            fadeIn2.play();
            FadeTransition fadeIn3 = new FadeTransition(Duration.seconds(1.0), img);
            fadeIn3.setFromValue(0.0);
            fadeIn3.setToValue(0.95);
            fadeIn3.play();
        });

        create.setOnFailed(workerStateEvent -> workerStateEvent.getSource().getException().printStackTrace());
        create.restart();


    }
}
