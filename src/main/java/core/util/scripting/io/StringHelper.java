package core.util.scripting.io;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
    private static Logger LOGGER = LogManager.getLogger(StringHelper.class.getSimpleName());

    private StringHelper() {
    }

    public static String getDateFromToday(int day, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        sdf.format(cal.getTime());
        String date = sdf.format(cal.getTime());
        LOGGER.info("Date = " + date);
        return date;
    }

    public static String getADateFromADay(String dueDate, int day, String formatDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat(formatDate);
        String dayAfterEngagementDeadline = "";
        try {
            Date dueDateEngagement = myFormat.parse(dueDate);
            long diff = dueDateEngagement.getTime() + day * 24 * 60 * 60 * 1000;
            Date today8 = new Date(diff);
            myFormat.applyPattern("dd-MM-yyyy");
            dayAfterEngagementDeadline = myFormat.format(today8);
            LOGGER.info("Diff: " + myFormat.format(today8));
            return dayAfterEngagementDeadline;

        } catch (ParseException e) {
            e.printStackTrace();
            return dayAfterEngagementDeadline;
        }
    }

    public static boolean isEmptyString(String input) {
        if ((null != input) && (!"".equals(input))) {
            return false;
        }
        return true;
    }

    public static String randomCharacters(String characters) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = Integer.parseInt(characters);
        int N = alphabet.length();
        Random r = new Random();
        String finalResult = "";
        for (int i = 0; i < length; i++) {
            char cha = alphabet.charAt(r.nextInt(N));
            String result = String.valueOf(cha);
            finalResult = finalResult + result;
        }
        LOGGER.info("Characters: " + finalResult);
        return finalResult;
    }

    public static String extractSubtring(String string, String pattern) {
        String theGroup = "";
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(string);
        if (mat.find()) {
            theGroup = mat.group(0);
            LOGGER.info(" Date string only: " + theGroup);
        }
        return theGroup;
    }

    public static String GetTimeStampValue() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss_SSS");
        String systime = sdf.format(new Date());
        systime = systime.replace(":", "");
        systime = systime.replace("-", "");
        return systime;
    }

    public static void replaceTextFile(String inputFile, String outFile, String key, String newKey) {
        try {
            Path path = Paths.get(inputFile);
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(key, newKey);
            Files.write(Paths.get(outFile), content.getBytes(charset));
        } catch (Exception e) {
            LOGGER.error("Got error: " + e.getMessage());
        }
    }

    public static String extractDigits(String inputString) {
        return inputString.replaceAll("[^0-9]", "");
    }

    public static String removeNumeric(String inputString) {
        return inputString.replaceAll("^\\d+", "").trim();
    }

    public static String getDecimal(String inputString) {
        return inputString.split("\\.")[0];
    }

    public static String getSubString(String inputString, int start, int length) {
        return inputString.substring(start, Math.min(start + length, inputString.length()));
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static List<Character> convertStringToCharList(String inputString) {
        List<Character> chars = new ArrayList<>();
        for (char c : inputString.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }

    public static String getSecondLine(String value) {
        return value.substring(value.indexOf("\n") + 1);
    }

}
