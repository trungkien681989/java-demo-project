package core.util.platform.mobile.android;

import core.util.platform.host.shell.CommandHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileHelper {

    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class);
    private static final String NO_SUCH_FILE_MESSAGE = "No such file or directory";
    private static final String NO_SUCH_FILE_LOG_MESSAGE = "File does not exist on android device";
    private final String adbCommand = "adb -s ";
    private final String deviceID;

    public FileHelper(String deviceID) {
        this.deviceID = deviceID;
    }
    
    public void removeFileFromDevice(String filePath) {
        String consoleOutput;
        consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " shell rm " + filePath);
        LOGGER.info(consoleOutput);
        if (consoleOutput.endsWith(NO_SUCH_FILE_MESSAGE)) {
            LOGGER.info(NO_SUCH_FILE_LOG_MESSAGE);
        } else LOGGER.info(filePath + " has removed on android device " + deviceID);
    }
    
    public void pushFileToDevice(String pathFileOnPC, String pathFileOnDevice) {
        String consoleOutput;
        LOGGER.info("Pushing file: " + pathFileOnPC + " to android device: " + deviceID);
        try {
            consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " push " + pathFileOnPC + " " + pathFileOnDevice);
            LOGGER.info(consoleOutput);
            if (consoleOutput.endsWith(NO_SUCH_FILE_MESSAGE)) {
                throw new FileNotFoundException(NO_SUCH_FILE_LOG_MESSAGE);
            } else LOGGER.info(pathFileOnPC + " has pushed to android device " + deviceID);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }
    
    public void pullFileFromDevice(String pathFileOnDevice, String pathFileOnPC) {
        String consoleOutput;
        try {
            consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " pull " + pathFileOnDevice + " " + pathFileOnPC);
            LOGGER.info(consoleOutput);
            if (consoleOutput.endsWith(NO_SUCH_FILE_MESSAGE)) {
                throw new FileNotFoundException(NO_SUCH_FILE_LOG_MESSAGE);
            } else LOGGER.info(pathFileOnDevice + " has pulled from android device " + deviceID);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }
    
    public List<String> getListFileInDir(String dir) {
        String consoleOutput;
        consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " shell ls -R " + dir);
        LOGGER.info(consoleOutput);
        consoleOutput = consoleOutput.replace(dir, "");
        return Arrays.asList(consoleOutput.split("\n"));
    }
    
    public void deleteAllFileInDir(String dir) {
        List<String> listFileName = getListFileInDir(dir);
        if (!listFileName.isEmpty()) {
            listFileName.forEach(e -> removeFileFromDevice(dir + e));
        }
    }
    
    public void createDirectoryInDevice(String pathOnDeviceToCreate) {
        String consoleOutput;
        consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " shell mkdir " + pathOnDeviceToCreate).replace("\n", "").trim();
        LOGGER.info(consoleOutput);
        if (consoleOutput.endsWith("File exists"))
            LOGGER.info("Directory: " + pathOnDeviceToCreate + " is Exists");
    }
    
    public void renameFileInDevice(String currentPath, String newPath) {
        String consoleOutput;
        consoleOutput = CommandHelper.executeCommand(adbCommand + deviceID + " shell mv " + currentPath + " " + newPath);
        LOGGER.info(consoleOutput);
        if (consoleOutput.endsWith(NO_SUCH_FILE_MESSAGE)) {
            LOGGER.info(NO_SUCH_FILE_LOG_MESSAGE);
        } else LOGGER.info(currentPath + " has removed on android device " + deviceID);
    }
}
