package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private static final String MODRINTH_PROJECT_ID = "nojGpzMF";
    private final ZlPvPTrigger plugin;

    public UpdateChecker(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        try {
            URL url = new URL("https://api.modrinth.com/v2/project/" + MODRINTH_PROJECT_ID + "/version");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String current = plugin.getDescription().getVersion();
                String latest = extractVersionNumber(response.toString());
                if (latest != null && !latest.equalsIgnoreCase(current)) {
                    plugin.getLogger().info("[ZlPvPTrigger] A new update is available!");
                    plugin.getLogger().info("Current version: " + current);
                    plugin.getLogger().info("Latest version: " + latest);
                    plugin.getLogger().info("Download: https://modrinth.com/plugin/zlpvptrigger");
                }
            }
        } catch (Exception ignored) {
            // Silent by design to avoid startup noise.
        }
    }

    private String extractVersionNumber(String json) {
        String key = "\"version_number\":\"";
        int index = json.indexOf(key);
        if (index < 0) {
            return null;
        }
        int start = index + key.length();
        int end = json.indexOf('"', start);
        if (end <= start) {
            return null;
        }
        return json.substring(start, end);
    }
}
