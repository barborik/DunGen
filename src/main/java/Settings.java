import java.io.*;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import com.google.gson.*;

public class Settings {
    File settingsFile;

    public void readSettings() {
        try {
            Gson gson = new Gson();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsFile));

            LinkedHashMap<String, Object> settings = gson.fromJson(bufferedReader.lines().collect(Collectors.joining()), LinkedHashMap.class);

            Window.width = (int) Math.floor((double) settings.get("width"));
            Window.height = (int) Math.floor((double) settings.get("height"));
            Camera.fov = (int) Math.floor((double) settings.get("fov"));
        } catch (FileNotFoundException ignored) {
        }
    }

    public void createSettings() {
        LinkedHashMap<String, Long> settings = new LinkedHashMap<>();
        settings.put("width", (long) Window.width);
        settings.put("height", (long) Window.height);
        settings.put("fov", (long) Camera.fov);

        Gson gson = new Gson();
        String json = gson.toJson(settings);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(settingsFile));
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException ignored) {
        }
    }

    public Settings() {
        settingsFile = new File("settings.json");
        if (settingsFile.exists() && !settingsFile.isDirectory()) {
            readSettings();
        } else {
            createSettings();
        }
    }
}
