package com.arizonsoftware.axtonsemotes.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

/**
 * BStats metrics helper.
 * Efficient, type-safe, and secure implementation.
 */
public class BStats {
   public static final int B_STATS_VERSION = 1;

   private static boolean logFailedRequests;
   private static boolean logSentData;
   private static boolean logResponseStatusText;
   private static String serverUUID;

   private final Plugin plugin;
   private final List<CustomChart> charts = new ArrayList<>();
   private final ScheduledExecutorService scheduler;

   public BStats(Plugin plugin, int pluginId) {
      if (plugin == null)
         throw new IllegalArgumentException("Plugin cannot be null!");

      this.plugin = plugin;
      this.scheduler = Executors.newScheduledThreadPool(1, r -> new Thread(r, "bStats-Metrics"));

      setupConfig();
      if (isEnabled()) {
         Bukkit.getServicesManager().register(BStats.class, this, plugin, ServicePriority.Normal);
         startSubmitting();
      }
   }

   /** Loads or creates bStats configuration */
   private void setupConfig() {
      File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
      if (!bStatsFolder.exists())
         bStatsFolder.mkdirs();
      File configFile = new File(bStatsFolder, "config.yml");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

      if (!config.isSet("serverUuid")) {
         config.addDefault("enabled", true);
         config.addDefault("serverUuid", UUID.randomUUID().toString());
         config.addDefault("logFailedRequests", false);
         config.addDefault("logSentData", false);
         config.addDefault("logResponseStatusText", false);
         config.options().copyDefaults(true);

         try {
            config.save(configFile);
         } catch (IOException ignored) {
         }
      }

      serverUUID = config.getString("serverUuid");
      logFailedRequests = config.getBoolean("logFailedRequests", false);
      logSentData = config.getBoolean("logSentData", false);
      logResponseStatusText = config.getBoolean("logResponseStatusText", false);
   }

   /** Returns whether metrics are enabled */
   public boolean isEnabled() {
      YamlConfiguration config = Configuration.getConfig("config.yml");
      return config != null && config.getBoolean("enable-metrics", true);
   }

   /** Adds a custom chart to this plugin */
   public void addCustomChart(CustomChart chart) {
      if (chart == null)
         throw new IllegalArgumentException("Chart cannot be null!");
      this.charts.add(chart);
   }

   /** Adds basic plugin metrics such as language */
   public void addMetricData(BStats metrics) {
      if (!isEnabled())
         return;

      YamlConfiguration config = Configuration.getConfig("config.yml");
      if (config == null || !config.getBoolean("enable-metrics"))
         return;

      String language = Objects.toString(config.getString("language"), "unknown");
      metrics.addCustomChart(new SimplePie("config_language", () -> language));
   }

   /** Prepares server data for submission */
   private JsonObject getServerData() {
      JsonObject data = new JsonObject();
      data.addProperty("serverUUID", serverUUID);
      data.addProperty("playerAmount", Bukkit.getOnlinePlayers().size());
      data.addProperty("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
      data.addProperty("bukkitVersion", Bukkit.getVersion());
      data.addProperty("bukkitName", Bukkit.getName());
      data.addProperty("javaVersion", System.getProperty("java.version"));
      data.addProperty("osName", System.getProperty("os.name"));
      data.addProperty("osArch", System.getProperty("os.arch"));
      data.addProperty("osVersion", System.getProperty("os.version"));
      data.addProperty("coreCount", Runtime.getRuntime().availableProcessors());
      return data;
   }

   /** Prepares plugin data for submission */
   private JsonArray getPluginData() {
      JsonArray pluginData = new JsonArray();
      for (CustomChart chart : charts) {
         JsonObject json = chart.getRequestJsonObject();
         if (json != null)
            pluginData.add(json);
      }
      return pluginData;
   }

   /** Starts the scheduled submission task */
   private void startSubmitting() {
      Runnable submitTask = () -> {
         if (!plugin.isEnabled())
            scheduler.shutdown();
         else
            Bukkit.getScheduler().runTask(plugin, this::submitData);
      };

      long initialDelay = TimeUnit.MINUTES.toMillis(3 + (long) (Math.random() * 3));
      long period = TimeUnit.MINUTES.toMillis(30);
      scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
      scheduler.scheduleAtFixedRate(submitTask, initialDelay, period, TimeUnit.MILLISECONDS);
   }

   /** Submits plugin + server data to bStats asynchronously */
   private void submitData() {
      JsonObject payload = getServerData();
      payload.add("plugins", getPluginData());

      Executors.newSingleThreadExecutor(r -> new Thread(r, "bStats-Submit")).submit(() -> {
         try {
            sendData(payload);
         } catch (Exception ex) {
            if (logFailedRequests)
               plugin.getLogger().log(Level.WARNING,
                     "Could not submit plugin stats of " + plugin.getName(), ex);
         }
      });
   }

   /** Sends JSON data to bStats with GZIP compression using modern HttpClient */
   private void sendData(JsonObject data) throws IOException, InterruptedException {
      if (data == null)
         throw new IllegalArgumentException("Data cannot be null!");
      if (Bukkit.isPrimaryThread())
         throw new IllegalStateException("Cannot submit data from main thread!");

      if (logSentData)
         plugin.getLogger().info("Sending data to bStats: " + data);

      // Compress JSON payload
      byte[] compressedData;
      try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(byteStream)) {
         gzip.write(data.toString().getBytes(StandardCharsets.UTF_8));
         gzip.finish();
         compressedData = byteStream.toByteArray();
      }

      // Build HTTP request
      HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://bStats.org/submitData/bukkit"))
            .header("Accept", "application/json")
            .header("Connection", "close")
            .header("Content-Encoding", "gzip")
            .header("Content-Type", "application/json")
            .header("User-Agent", "MC-Server/1")
            .POST(HttpRequest.BodyPublishers.ofByteArray(compressedData))
            .build();

      // Send request
      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (logResponseStatusText)
         plugin.getLogger().info("bStats response: " + response.body());
   }

   /* ====== Charts ====== */
   public abstract static class CustomChart {
      final String chartId;

      protected CustomChart(String chartId) {
         if (chartId == null || chartId.isEmpty())
            throw new IllegalArgumentException("ChartId cannot be null/empty!");
         this.chartId = chartId;
      }

      private JsonObject getRequestJsonObject() {
         try {
            JsonObject data = getChartData();
            if (data == null)
               return null;
            JsonObject chart = new JsonObject();
            chart.addProperty("chartId", chartId);
            chart.add("data", data);
            return chart;
         } catch (Exception e) {
            if (logFailedRequests)
               Bukkit.getLogger().log(Level.WARNING,
                     "Failed to get data for custom chart " + chartId, e);
            return null;
         }
      }

      protected abstract JsonObject getChartData() throws Exception;
   }

   public static class SimplePie extends CustomChart {
      private final Callable<String> callable;

      public SimplePie(String chartId, Callable<String> callable) {
         super(chartId);
         this.callable = callable;
      }

      @Override
      protected JsonObject getChartData() throws Exception {
         String value = callable.call();
         if (value == null || value.isEmpty())
            return null;
         JsonObject data = new JsonObject();
         data.addProperty("value", value);
         return data;
      }
   }

   public static class SingleLineChart extends CustomChart {
      private final Callable<Integer> callable;

      public SingleLineChart(String chartId, Callable<Integer> callable) {
         super(chartId);
         this.callable = callable;
      }

      @Override
      protected JsonObject getChartData() throws Exception {
         int value = callable.call();
         if (value == 0)
            return null;
         JsonObject data = new JsonObject();
         data.addProperty("value", value);
         return data;
      }
   }

}
