package core.util.platform.mobile.android;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    private final List<String> permissionList;

    private PermissionManager(PermissionManager.PermissionBuilder builder) {
        this.permissionList = builder.getPermissionContexts();
    }

    public static void grantPermissions(PermissionManager androidPermissionManager, String deviceID,
            String appPackage) {
        // DeviceHelper deviceConfiguration = DeviceHelper.getInstance();
        // if (deviceConfiguration.isNeedToGrantPermission(deviceID)) {
        // androidPermissionManager.getPermissions().forEach((permission) -> {
        // deviceConfiguration.grantPermission(permission, appPackage, deviceID);
        // });
        // }
    }

    public List<String> getPermissions() {
        return this.permissionList;
    }

    public static class PermissionBuilder {
        private final List<String> permissionList = new ArrayList<>();

        public PermissionBuilder() {
        }

        public PermissionManager.PermissionBuilder addPermission(String permissionContext) {
            this.permissionList.add(permissionContext);
            return this;
        }

        List<String> getPermissionContexts() {
            return this.permissionList;
        }

        public PermissionManager build() {
            return new PermissionManager(this);
        }
    }
}
