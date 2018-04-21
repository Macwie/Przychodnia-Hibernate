package src.main;

import javafx.util.StringConverter;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;

public class TimeStampConverter extends StringConverter<Timestamp> {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    @Override
    public String toString(Timestamp object) {
        if (object == null) {
            return "";
        } else {
            String date = df.format(object);
            return date;
        }
    }

    @Override
    public Timestamp fromString(String string) {
        try {
            return Timestamp.valueOf(string);
        } catch (DateTimeParseException exc) {
            return null ;
        }
    }
}