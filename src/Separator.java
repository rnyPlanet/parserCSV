import java.util.Arrays;
import java.util.stream.Stream;

public enum Separator {

    SEMICOLON(";"),
    COMMA(","),
    COLON(":");

    private String separator;

    Separator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

    public static String[] getNames() {
        return Stream.of(Separator.values()).map(Separator::getSeparator).toArray(String[]::new);
    }

}
