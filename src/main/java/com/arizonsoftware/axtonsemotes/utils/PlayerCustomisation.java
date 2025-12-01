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

public class PlayerCustomisation {

   /**
    * Stores a player-specific setting in a JSON file.
    *
    * @param source The name of the data source (e.g., "toggleemotes"). Determines the filename.
    * @param player The player whose data is being stored.
    * @param data   The data to store. Can be any object compatible with Gson serialization.
    */
   public static void storePlayerSetting(String source, Player player, Object data) {
      File playerDataFolder = new File(AxtonsEmotes.getInstance().getDataFolder(), "playerdata");
      if (!playerDataFolder.exists()) {
         playerDataFolder.mkdirs();
      }

      File playerDataFile = new File(playerDataFolder, source + ".json");

      try (FileWriter writer = new FileWriter(playerDataFile)) {
         Gson gson = new GsonBuilder().setPrettyPrinting().create();
         JsonObject playerData = new JsonObject();
         playerData.add(player.getUniqueId().toString(), gson.toJsonTree(data));
         gson.toJson(playerData, writer);
      } catch (IOException e) {
         e.printStackTrace();
      }

      Debugging.log(
            PlayerCustomisation.class.getSimpleName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
            "Updated " + source + " settings for player " + player.getName());
   }

   /**
    * Retrieves a boolean player-specific setting from a JSON file.
    *
    * @param source The name of the data source (e.g., "toggleemotes"). Determines the filename.
    * @param player The player whose setting is being retrieved.
    * @return The stored Boolean value, or null if the file or player entry does not exist.
    */
   public static Boolean retrieveBooleanPlayerSetting(String source, Player player) {
      File playerDataFile = new File(AxtonsEmotes.getInstance().getDataFolder(), "playerdata/" + source + ".json");
      if (!playerDataFile.exists()) {
         return null;
      }

      try (FileReader reader = new FileReader(playerDataFile)) {
         Gson gson = new Gson();
         JsonObject playerData = gson.fromJson(reader, JsonObject.class);
         if (playerData.has(player.getUniqueId().toString())) {
            Boolean value = gson.fromJson(playerData.get(player.getUniqueId().toString()), Boolean.class);
            Debugging.log(
                  PlayerCustomisation.class.getSimpleName() + "/"
                        + Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Retrieved " + source + " settings for player " + player.getName());
            return value;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }
}
