package core.util.reporting.report;

public class HtmlFontHelper {
    private HtmlFontHelper() {
    }

    public static String convertToRedBold(String strMsg) {
        return "<b><font color='red'>" + strMsg + "</font>" + "</b>";
    }

    public static String convertToRed(String strMsg) {
        return "<font color='red'>" + strMsg + "</font>";
    }

    public static String convertToOrangeBold(String strMsg) {
        return "<b><font color='orange'>" + strMsg + "</font>" + "</b>";
    }

    public static String convertToOrange(String strMsg) {
        return "<font color='orange'>" + strMsg + "</font>";
    }

    public static String convertToYellowBold(String strMsg) {
        return "<b><font color='yellow'>" + strMsg + "</font>" + "</b>";
    }

    public static String convertToYellow(String strMsg) {
        return "<font color='yellow'>" + strMsg + "</font>";
    }

    public static String convertToBlueBold(String strMsg) {
        return "<b><font color='cyan'>" + strMsg + "</font>" + "</b>";
    }

    public static String convertToBlue(String strMsg) {
        return "<font color='cyan'>" + strMsg + "</font>";
    }

    public static String convertToGreenBold(String strMsg) {
        return "<b><font color='lime'>" + strMsg + "</font>" + "</b>";
    }

    public static String convertToGreen(String strMsg) {
        return "<font color='lime'>" + strMsg + "</font>";
    }

    public static String convertToBold(String strMsg) {
        return "<b>" + strMsg + "</b>";
    }

    public static String convertToTestCaseLink(String strTestCaseName) {
        return "<a href=" + System.getProperty("jiraSite") + strTestCaseName + ">" + strTestCaseName + "</a>";
    }
}
