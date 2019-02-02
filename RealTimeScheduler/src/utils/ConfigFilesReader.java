package utils;

import exception.MalformedConfigFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public final class ConfigFilesReader {

    private static final String CONFIG_FILES_LOCATION = System.getProperty("user.dir") + "\\RealTimeScheduler\\src\\config";

    public static ArrayList<String> getAvailableFileNames() {
        ArrayList<String> fileNames = new ArrayList<>();
        File configDirectory        = new File(CONFIG_FILES_LOCATION);

        if (configDirectory.isDirectory()) {

            for (File file : Objects.requireNonNull(configDirectory.listFiles())) {
                fileNames.add(file.getName());
            }
        }

        return fileNames;
    }

    public static ConfigFile readConfigFile(String fileName) throws MalformedConfigFileException, IOException {
        File file = new File(CONFIG_FILES_LOCATION + "/" + fileName);
        ConfigFile configFile = new ConfigFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        try {

            if (line != null) {
                String[] firstLineArr = line.split(",");
                String scheduler = firstLineArr[0];
                String executionTime = "";

                if (firstLineArr.length > 1 ){
                    executionTime = firstLineArr[1] == null ? "" : firstLineArr[1];
                }

                configFile.setScheduler(scheduler);
                configFile.setExecutionTime(executionTime);
            }

            while((line = bufferedReader.readLine()) != null) {
                String[] lineArr = line.split(",");
                String id = lineArr[0];
                String period = lineArr[1];
                String execution = lineArr[2];

                configFile.addTask(period, execution, id);
            }

            return configFile;

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MalformedConfigFileException();
        }
    }
}
