package core.util.platform.host.shell;

import core.util.platform.host.os.OsHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandHelper {
    private static final Logger LOGGER = LogManager.getLogger(CommandHelper.class);

    private CommandHelper() {
    }

    public static String executeCommand(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder strAllLines = new StringBuilder();
            while ((line = r.readLine()) != null) {
                strAllLines = strAllLines.append("").append(line).append("\n");
                if (line.contains("Console LogLevel: debug") && line.contains("Complete")) {
                    break;
                }
            }
            return strAllLines.toString();
        } catch (IOException ex) {
            LOGGER.error("Kill Process Error: ", ex);
        }
        return null;
    }

    // TODO : Review these usage
    public static String executeShellScript(String script) {
        try {
            BufferedReader br = getBufferedReader(script);
            String line;
            StringBuilder strAllLines = new StringBuilder();
            while ((line = br.readLine()) != null) {
                strAllLines = strAllLines.append("").append(line).append("\n");
                LOGGER.info(strAllLines);
            }
            return strAllLines.toString().split(":")[1].replace("\n", "").trim();
        } catch (IOException ex) {
            LOGGER.error("Execute Command Through Process Builder Error: ", ex);
        }
        return null;
    }

    public static String executeShellScriptToGetDeviceID(String command) {
        try {
            BufferedReader br = getBufferedReader(command);
            String line;
            String allLine = "";
            while ((line = br.readLine()) != null) {
                allLine = allLine.trim() + "" + line.trim() + "\n";
            }
            return allLine.trim();
        } catch (IOException ex) {
            LOGGER.error("Get DeviceID Error: ", ex);
        }
        return null;
    }

    public static void killProcess(String serviceName) {
        String command = "";
        if (OsHelper.isWindows()) {
            command = "taskkill /f /IM ";
        } else if (OsHelper.isUnix()) {
            command = "killall -SIGKILL ";
        }
        while (true) {
            try {
                if (isProcessRunning(serviceName)) {
                    String commandLine = command + serviceName;
                    LOGGER.debug("Execute command line: [" + commandLine + "]");
                    Runtime.getRuntime().exec(commandLine);
                    continue;
                }
            } catch (IOException ex) {
                LOGGER.error("Kill Process Error: ", ex);
            }
            return;
        }
    }

    public static boolean isProcessRunning(String serviceName) {
        String command = "";
        if (OsHelper.isWindows()) {
            command = "tasklist";
        } else if (OsHelper.isUnix()) {
            command = "ps -a";
        }else if (OsHelper.isMac()){
            command = "ps -ax";
        }
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            do {
                if ((line = reader.readLine()) == null) {
                    return false;
                }
            } while (!line.contains(serviceName));

            LOGGER.info(serviceName + " is running ...");
            return true;
        } catch (IOException ex) {
            LOGGER.error("Get Process Running Error: ", ex);
            return false;
        }
    }

    private static BufferedReader getBufferedReader(String command) {

        try {
            List<String> commands = new ArrayList<>();
            if (OsHelper.isUnix()) {
                commands.add("/bin/sh");
                commands.add("-c");
                commands.add(command);
            } else if (OsHelper.isWindows()) {
                commands.add(command);
            }

            ProcessBuilder builder = new ProcessBuilder(commands);
            final Process process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            return new BufferedReader(isr);
        } catch (IOException ex) {
            LOGGER.error("Get Buffered Reader Error: ", ex);
        }
        return null;
    }
}
