import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Spliter {

    private static char separator = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void main(String[] args) throws Exception {

        String sourceFile = "C:\\Users\\worka\\Desktop\\" + "" + ".csv";

        parse(sourceFile);

    }

    private static void parse(String sourceFile) throws FileNotFoundException, WrongSeparatorException {
        boolean isSeparatorFound = false;

        Scanner scanner = new Scanner(new File(sourceFile));

        while (scanner.hasNext()) {
            String scannerLine = scanner.nextLine();

            if (!isSeparatorFound) {
                separator = searchSeparator(scannerLine).toCharArray()[0];
                isSeparatorFound = true;
            }

            List<String> parsedLine = parseLine(scannerLine);

            for (String line : parsedLine) {
                System.out.println(line);
            }

            System.out.println();
        }
        scanner.close();
    }

    private static String searchSeparator(String line) throws WrongSeparatorException {

        for (String separator : Separator.getNames()) {
            if (line.contains(separator)) {
                return separator;
            }
        }

        throw new WrongSeparatorException();
    }

    public static List<String> parseLine(String line) {
        return parseLine(line, separator, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String line, char separators, char customQuote) {

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
