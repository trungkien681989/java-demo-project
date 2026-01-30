package core.util.platform.android.permission;

public enum AndroidPermission {
    ANDROID_DEVICE_PERMISSION_FILE_PATH("core/android/android-device-permission.yaml"),
    READ_EXTERNAL_STORAGE("readExternalStorage"),
    WRITE_EXTERNAL_STORAGE("writeExternalStorage"),
    CAMERA("camera"),
    ACCESS_FINE_LOCATION("accessFineLocation"),
    ACCESS_COARSE_LOCATION("accessCoarseLocation"),
    READ_PHONE_STATE("readPhoneState");

    private String key;

    AndroidPermission(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
