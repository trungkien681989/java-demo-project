package core.util.platform.host.file;

import core.util.platform.host.os.OsHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileHelper {
    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class.getSimpleName());

    private FileHelper() {
    }

    /**
     * Convert directory path based on OS type
     */
    public static String convertDirectory(String directory) {
        if (directory == null || directory.isEmpty()) {
            LOGGER.warn("Directory path is null or empty");
            return directory;
        }

        if (OsHelper.isWindows()) {
            LOGGER.info("Working on Windows environment: " + OsHelper.getOsFullName());
            directory = directory.replace("/", "\\");
        } else {
            LOGGER.info("Working on Linux / Mac environment: " + OsHelper.getOsFullName());
        }
        return directory;
    }

    /**
     * Delete all files matching pattern in folder
     */
    public static void deleteAllFilesInFolder(String folderPath) {
        if (folderPath == null || folderPath.isEmpty()) {
            LOGGER.warn("Folder path is null or empty");
            return;
        }

        final File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            LOGGER.warn("Path is not a directory: " + folderPath);
            return;
        }

        final File[] files = folder.listFiles((dir, name) -> name.matches("revert.*\\.sql"));

        if (files == null || files.length == 0) {
            LOGGER.info("No files to delete in: " + folderPath);
            return;
        }

        for (final File file : files) {
            if (file.delete()) {
                LOGGER.info("Deleted file: " + file.getAbsolutePath());
            } else {
                LOGGER.error("Failed to delete: " + file.getAbsolutePath());
            }
        }
    }

    /**
     * Delete screenshots from ReportNG folder
     */
    public static void deleteScreenshots() {
        try {
            String downloadFolderPath = System.getProperty("user.dir") + "/ReportNGScreenShots";
            File file = new File(downloadFolderPath);

            if (!file.exists() || !file.isDirectory()) {
                LOGGER.info("Screenshots folder does not exist: " + downloadFolderPath);
                return;
            }

            File[] listOfFiles = file.listFiles();
            if (listOfFiles == null || listOfFiles.length == 0) {
                LOGGER.info("No screenshots to delete");
                return;
            }

            for (File f : listOfFiles) {
                if (f.isFile() && f.delete()) {
                    LOGGER.info("Deleted screenshot: " + f.getName());
                } else {
                    LOGGER.warn("Failed to delete: " + f.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting screenshots: " + e.getMessage(), e);
        }
    }

    /**
     * Replace text in file
     */
    public static void replaceTextFile(String inputFile, String outFile, String key, String newKey) {
        try {
            Path path = Paths.get(inputFile);
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(key, newKey);
            Files.write(Paths.get(outFile), content.getBytes(charset));
            LOGGER.info("File updated: " + outFile);
        } catch (Exception e) {
            LOGGER.error("Error replacing text in file: " + e.getMessage(), e);
        }
    }

    /**
     * Unzip file to download folder
     */
    public static boolean unzipFileOnDownloadFolder(String fileNameZip, String downloadFolder) {
        String zipFilePath = convertDirectory(downloadFolder.concat(fileNameZip));
        String destDir = convertDirectory(downloadFolder);
        File dir = new File(destDir);

        if (!dir.exists() && !dir.mkdirs()) {
            LOGGER.error("Failed to create directory: " + destDir);
            return false;
        }

        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(zipFilePath);
                ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                String fileName = ze.getName();
                LOGGER.info("Unzipping: " + fileName);
                File newFile = new File(destDir + File.separator + fileName);

                // Create parent directories
                File parent = newFile.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    LOGGER.warn("Failed to create parent directory: " + parent.getAbsolutePath());
                }

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
            }
            LOGGER.info("Unzip completed successfully");
            return true;
        } catch (IOException e) {
            LOGGER.error("Error unzipping file: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verify only specified files exist in zip
     */
    public static boolean verifyOnlyFileOnZipDownloadFile(List<String> listFileContainName, String filePath) {
        if (listFileContainName == null || listFileContainName.isEmpty()) {
            LOGGER.warn("File list is null or empty");
            return false;
        }

        try (ZipFile sourceZipFile = new ZipFile(filePath)) {
            LOGGER.info("Zip file size: " + sourceZipFile.size());
            LOGGER.info("Searching for: " + listFileContainName);

            if (listFileContainName.size() != sourceZipFile.size()) {
                LOGGER.error("File count mismatch. Expected: " + listFileContainName.size() +
                        ", Found: " + sourceZipFile.size());
                return false;
            }

            for (String fileContainName : listFileContainName) {
                if (!searchFileOnZipDownloadFile(fileContainName, filePath)) {
                    LOGGER.error("File not found: " + fileContainName);
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error verifying zip file: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Search for file in zip
     */
    public static boolean searchFileOnZipDownloadFile(String fileContainName, String filePath) {
        if (fileContainName == null || fileContainName.isEmpty()) {
            LOGGER.warn("File name is null or empty");
            return false;
        }

        try (ZipFile sourceZipFile = new ZipFile(filePath)) {
            Enumeration<? extends ZipEntry> entries = sourceZipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains(fileContainName)) {
                    LOGGER.info("Found: " + entry.getName());
                    return true;
                }
            }
            LOGGER.warn("File not found in zip: " + fileContainName);
            return false;
        } catch (IOException e) {
            LOGGER.error("Error searching in zip file: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Delete all files in directory
     */
    public static void deleteAllFileExists(String pathLocation) {
        if (pathLocation == null || pathLocation.isEmpty()) {
            LOGGER.warn("Path location is null or empty");
            return;
        }

        File file = new File(pathLocation);
        if (!file.isDirectory()) {
            LOGGER.warn("Path is not a directory: " + pathLocation);
            return;
        }

        String[] myFiles = file.list();
        if (myFiles == null || myFiles.length == 0) {
            LOGGER.info("Directory is empty: " + pathLocation);
            return;
        }

        for (String filename : myFiles) {
            File myFile = new File(file, filename);
            if (myFile.delete()) {
                LOGGER.info("Deleted: " + filename);
            } else {
                LOGGER.warn("Failed to delete: " + filename);
            }
        }
    }

    /**
     * Check if file exists with optional deletion
     */
    public static boolean checkFileExists(String pathLocation, boolean deleteExisted) {
        if (pathLocation == null || pathLocation.isEmpty()) {
            LOGGER.warn("Path location is null or empty");
            return false;
        }

        Path path = Paths.get(pathLocation);
        try {
            if (Files.exists(path)) {
                if (deleteExisted) {
                    Files.delete(path);
                    boolean deleted = !Files.exists(path);
                    if (deleted) {
                        LOGGER.info("File deleted: " + pathLocation);
                    }
                    return deleted;
                }
                return true;
            }
        } catch (IOException ex) {
            LOGGER.error("Error checking/deleting file: " + ex.getMessage(), ex);
        }
        return false;
    }

    /**
     * Calculate MD5 checksum of file
     */
    public static String calculateMD5(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            LOGGER.warn("File path is null or empty");
            return null;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LOGGER.error("File does not exist: " + filePath);
                return null;
            }

            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            return getFileChecksum(md5Digest, file);
        } catch (NoSuchAlgorithmException | IOException e) {
            LOGGER.error("Error calculating MD5: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Attach file via clipboard and keyboard automation
     */
    public static void attachFile(String commentFile, String folder) {
        try {
            String pathAttachLocation = convertDirectory(folder);
            LOGGER.info("Attaching file from: " + pathAttachLocation);

            StringSelection pathSelection = new StringSelection(pathAttachLocation);
            StringSelection commentSelection = new StringSelection(commentFile);
            Robot robot = new Robot();

            // Paste path
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pathSelection, null);
            Thread.sleep(500);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(500);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Paste comment
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(commentSelection, null);
            Thread.sleep(500);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(500);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(1000);

            LOGGER.info("File attached successfully");
        } catch (Exception e) {
            LOGGER.error("Error attaching file: " + e.getMessage(), e);
        }
    }

    /**
     * Get value from properties file
     */
    public static String getValuePropertiesFile(String filePath, String key) {
        if (filePath == null || key == null) {
            LOGGER.warn("File path or key is null");
            return null;
        }

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            prop.load(input);
            String value = prop.getProperty(key);
            LOGGER.info("Property " + key + " = " + value);
            return value;
        } catch (FileNotFoundException e) {
            LOGGER.error("Properties file not found: " + filePath, e);
        } catch (IOException e) {
            LOGGER.error("Error reading properties file: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Set value in properties file
     */
    public static void setValuePropertiesFile(String filePath, String key, String value) {
        if (filePath == null || key == null || value == null) {
            LOGGER.warn("File path, key, or value is null");
            return;
        }

        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath);
                FileOutputStream fos = new FileOutputStream(filePath)) {

            prop.load(fis);
            prop.setProperty(key, value);
            prop.store(fos, "Updated: " + key);
            LOGGER.info("Property set: " + key + " = " + value);
        } catch (FileNotFoundException e) {
            LOGGER.error("Properties file not found: " + filePath, e);
        } catch (IOException e) {
            LOGGER.error("Error updating properties file: " + e.getMessage(), e);
        }
    }

    /**
     * Get CSV file data
     */
    public static String getCSVFile(String fileName, String filePath) {
        if (fileName == null || filePath == null) {
            LOGGER.warn("File name or path is null");
            return null;
        }

        try {
            String csvFile = filePath + File.separator + fileName + ".csv";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line = br.readLine();
                if (line != null) {
                    String[] csvData = line.split(",");
                    LOGGER.info("CSV data: " + csvData[0]);
                    return csvData[0];
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("CSV file not found: " + fileName, e);
        } catch (IOException e) {
            LOGGER.error("Error reading CSV file: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Verify file download and checksum
     */
    public static void verifyDownloadFileSuccessAndChecksum(String fileName, String folderDownload) {
        String filePath = convertDirectory(folderDownload.concat(fileName));
        boolean fileExisted = checkFileExists(filePath, false);

        Assert.assertTrue(fileExisted, "File should exist: " + fileName);

        if (fileExisted) {
            String checkMd5DownloadFile = calculateMD5(filePath);
            LOGGER.info("MD5 checksum: " + checkMd5DownloadFile);
            Assert.assertNotNull(checkMd5DownloadFile, "MD5 should not be null");
        }
    }

    /**
     * Verify download file does not exist
     */
    public static void verifyDownloadListFilesIsNotDownload(String fileName, String folderDownload) {
        String filePath = convertDirectory(folderDownload.concat(fileName));
        boolean fileExisted = checkFileExists(filePath, false);
        Assert.assertFalse(fileExisted, "File should not exist: " + fileName);
    }

    /**
     * Read text file content
     */
    public static String readContextTextFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.warn("File name is null or empty");
            return "";
        }

        try {
            return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Error reading file: " + fileName, e);
            return "";
        }
    }

    /**
     * Calculate file checksum
     */
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
    }

    /**
     * Create directory
     */
    public static void createDirectory(File destFolder) {
        if (destFolder == null) {
            LOGGER.warn("Destination folder is null");
            return;
        }

        if (destFolder.exists()) {
            deleteDirectory(destFolder);
        }

        if (!destFolder.mkdirs()) {
            LOGGER.warn("Failed to create directory: " + destFolder.getAbsolutePath());
        } else {
            LOGGER.info("Directory created: " + destFolder.getAbsolutePath());
        }
    }

    /**
     * Copy directory recursively
     */
    public static void copyDirectory(File srcFolder, File destFolder) {
        if (srcFolder == null || destFolder == null) {
            LOGGER.warn("Source or destination folder is null");
            return;
        }

        if (!srcFolder.exists()) {
            LOGGER.error("Source folder does not exist: " + srcFolder.getAbsolutePath());
            return;
        }

        if (srcFolder.isDirectory()) {
            if (!destFolder.exists() && !destFolder.mkdirs()) {
                LOGGER.error("Failed to create destination directory");
                return;
            }

            String[] files = srcFolder.list();
            if (files != null) {
                for (String file : files) {
                    copyDirectory(new File(srcFolder, file), new File(destFolder, file));
                }
            }
        } else {
            try (InputStream in = new FileInputStream(srcFolder);
                    OutputStream out = new FileOutputStream(destFolder)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                LOGGER.info("File copied: " + srcFolder.getName());
            } catch (IOException e) {
                LOGGER.error("Error copying file: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Delete directory recursively
     */
    public static void deleteDirectory(File file) {
        if (file == null || !file.exists()) {
            LOGGER.warn("File is null or does not exist");
            return;
        }

        if (file.isDirectory()) {
            String[] files = file.list();
            if (files != null) {
                for (String temp : files) {
                    deleteDirectory(new File(file, temp));
                }
            }
        }

        if (file.delete()) {
            LOGGER.info("Deleted: " + file.getAbsolutePath());
        } else {
            LOGGER.warn("Failed to delete: " + file.getAbsolutePath());
        }
    }

    /**
     * Get all files with specific extension
     */
    public static List<File> getAllFileWithExtension(String dir, String extension) {
        if (dir == null || extension == null) {
            LOGGER.warn("Directory or extension is null");
            return Collections.emptyList();
        }

        List<File> listCurrentFileInDir = getAllFileInFolder(dir);
        return listCurrentFileInDir.isEmpty() ? Collections.emptyList()
                : listCurrentFileInDir.stream()
                        .filter(f -> f.getAbsolutePath().endsWith(extension))
                        .collect(Collectors.toList());
    }

    /**
     * Get all files in folder
     */
    public static List<File> getAllFileInFolder(String dir) {
        if (dir == null || dir.isEmpty()) {
            LOGGER.warn("Directory is null or empty");
            return Collections.emptyList();
        }

        try {
            try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
                return paths.filter(p -> Files.isRegularFile(p, new LinkOption[0]))
                        .map(Path::toFile)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            LOGGER.error("Error listing files in directory: " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Get all file names from file list
     */
    public static List<String> getAllFileNameInFileList(List<File> fileList) {
        if (fileList == null) {
            LOGGER.warn("File list is null");
            return Collections.emptyList();
        }

        return fileList.stream()
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }
}
