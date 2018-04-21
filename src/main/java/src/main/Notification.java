package src.main;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {

    public static Notifications makeNotification(String title,String text,int durationInSeconds){
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.seconds(durationInSeconds))
                .position(Pos.BASELINE_RIGHT)
                .darkStyle();
        return notificationBuilder;
    }
}
