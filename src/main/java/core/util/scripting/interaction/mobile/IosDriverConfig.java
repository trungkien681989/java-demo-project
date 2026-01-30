package core.util.scripting.interaction.mobile;

public enum IosDriverConfig {
    IOS_DRIVER_CONFIG_FILE_PATH("core/appium/ios/ios-driver.yaml"),
    IOS_DRIVER_IMPLICITLY_WAIT("iosDriverImplicitlyWait"),
    IOS_ELEMENT_LIMIT_WAIT("iosElementLimitWait"),
    IOS_ELEMENT_POLLING_WAIT("iosElementPollingWait"),
    IOS_ELEMENT_MINI_WAIT("iosElementMiniWait");

    private final String key;

    IosDriverConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
