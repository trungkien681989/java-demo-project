package core.util.platform.mobile.android;

public enum Permission {
    ANDROID_DEVICE_PERMISSION_FILE_PATH("core/appium/android/android-device-permission.yaml"),
    READ_EXTERNAL_STORAGE("readExternalStorage"),
    WRITE_EXTERNAL_STORAGE("writeExternalStorage"),
    CAMERA("camera"),
    ACCESS_FINE_LOCATION("accessFineLocation"),
    ACCESS_COARSE_LOCATION("accessCoarseLocation"),
    READ_PHONE_STATE("readPhoneState");

    private final String key;

    Permission(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
