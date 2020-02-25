import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Spliter {

    private static char separator = ',';
    private static final char DEFAULT_QUOTE = '"';

    public List<List<String>> parse(String sourceFile) throws FileNotFoundException, WrongSeparatorException {
        boolean isSeparatorFound = false;

        List<List<String>> rowsFromFile = new ArrayList<>();

        Scanner scanner = new Scanner(new File(sourceFile));
        while (scanner.hasNext()) {
            String scannerRow = scanner.nextLine();

            if (!isSeparatorFound) {
                separator = searchSeparator(scannerRow).toCharArray()[0];
                isSeparatorFound = true;
            }

            List<String> parsedRow = parseRow(scannerRow);
            rowsFromFile.add(parsedRow);
        }

        scanner.close();

        return rowsFromFile;
    }

    private static String searchSeparator(String line) throws WrongSeparatorException {

        for (String separator : Separator.getNames()) {
            if (line.contains(separator)) {
                return separator;
            }
        }

        throw new WrongSeparatorException();
    }

    private static List<String> parseRow(String line) {
        return parseRow(line, separator, DEFAULT_QUOTE);
    }

    private static List<String> parseRow(String line, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        if (line == null || line.isEmpty()) {
            return result;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;

        char[] chars;

        if (line.contains("\"\"")) {
            chars = line.substring(1, line.length() - 1).replaceAll("\"\"", "\"").toCharArray();
        } else {
            chars = line.toCharArray();
        }

        for (char ch : chars) {

            if (inQuotes) {
                if (ch == customQuote) {
                    inQuotes = false;
                } else {
                    curVal.append(ch);
                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
                } else if (ch == separators) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }


}
