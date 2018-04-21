package src.main;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.hibernate.Session;

public class CreateSession extends Service<Session> {
    @Override
    protected Task<Session> createTask() {

        return new Task<Session>() {
            @Override
            protected Session call() throws Exception {

                return HibernateUtil.getInstance();
            }
        };
    }
}
