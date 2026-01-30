package core.util.reporting.report;

public enum ScreenshotConfig {
    DEBUG("debug"),
    ;

    private String key;

    ScreenshotConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public static boolean debug() {
        if (System.getProperty(ScreenshotConfig.DEBUG.toString()) != null) {
            return Boolean.parseBoolean(System.getProperty(ScreenshotConfig.DEBUG.toString()));
        } else return false;
    }
}
