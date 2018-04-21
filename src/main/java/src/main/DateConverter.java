package src.main;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import javafx.util.StringConverter;

public class DateConverter extends StringConverter<Date>{
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public String toString(Date t) {
        if (t == null) {
            return "";
        } else {
            String date = df.format(t);
            return date;
        }
    }
    @Override
    public Date fromString(String string) {
        try {
            return Date.valueOf(string);
        } catch (DateTimeParseException exc) {
            return null ;
        }
    }
}
