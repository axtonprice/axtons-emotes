package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.entity.Player;

public class PlayerData {

    /**
    * Stores a player-specific setting in a JSON file.
    *
    * @param source The name of the data source (e.g., "toggleemotes"). Determines the filename.
    * @param player The player whose data is being stored.
    * @param data   The data to store. Can be any object compatible with Gson serialization.
    */
    public static void storePlayerSetting(String source, Player player, Object data) {

        // Ensure playerdata directory exists
        File playerDataFolder = new File(AxtonsEmotes.getInstance().getDataFolder(), "playerdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }

        File playerDataFile = new File(playerDataFolder, source + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject playerData = new JsonObject();

        // Load existing JSON if it exists
        if (playerDataFile.exists()) {
            try (FileReader reader = new FileReader(playerDataFile)) {
                playerData = gson.fromJson(reader, JsonObject.class);

                // If file was empty or invalid, safeguard
                if (playerData == null) {
                    playerData = new JsonObject();
                }
            } catch (Exception ignored) {
            }
        }

        // Append / update entry
        playerData.add(player.getUniqueId().toString(), gson.toJsonTree(data));

        // Write back to file
        try (FileWriter writer = new FileWriter(playerDataFile)) {
            gson.toJson(playerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Debugging.log(
                PlayerData.class.getSimpleName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Updated " + source + " settings for player " + player.getName());
    }

    /**
     * Retrieves a specific player setting from a JSON file based on the provided source and player.
     *
     * @param source the name of the setting source (used as the JSON filename)
     * @param player the {@link Player} whose setting is to be retrieved
     * @param clazz the {@link Class} type to deserialize the setting into
     * @param <T> the type of the setting to be returned
     * @return the deserialized player setting of type {@code T}, or {@code null} if not found or on error
     */
    public static <T> T retrievePlayerSetting(String source, Player player, Class<T> clazz) {
        File playerDataFile = new File(AxtonsEmotes.getInstance().getDataFolder(), "playerdata/" + source + ".json");
        if (!playerDataFile.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(playerDataFile)) {
            Gson gson = new Gson();
            JsonObject playerData = gson.fromJson(reader, JsonObject.class);

            String uuid = player.getUniqueId().toString();

            if (playerData.has(uuid)) {
                T value = gson.fromJson(playerData.get(uuid), clazz);

                Debugging.log(
                        PlayerData.class.getSimpleName() + "/retrievePlayerSetting",
                        "Retrieved " + source + " settings for player " + player.getName());

                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
