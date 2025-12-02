package net.lixir.vminus.data.server.bans;

import com.google.gson.*;
import net.lixir.vminus.Vminus;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BannedRecipeManager extends JsonDataLoader {
    public static final BannedRecipeManager INSTANCE = new BannedRecipeManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Set<Identifier> bannedRecipes = new HashSet<>();

    private BannedRecipeManager() {
        super(GSON, "bans/recipes");
    }

    @Override
    protected void apply(@NotNull Map<Identifier, JsonElement> objects,
                         ResourceManager resourceManager,
                         Profiler profiler) {
        bannedRecipes.clear();

        // Load datapack JSONs
        for (Map.Entry<Identifier, JsonElement> entry : objects.entrySet()) {
            try {
                processJson(entry.getValue().getAsJsonObject());
            } catch (Exception e) {
                Vminus.LOGGER.error("Error processing datapack recipe ban '{}':", entry.getKey(), e);
            }
        }

        // Load config directory JSONs
        /*
        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "bans/recipes");
        if (configDir.exists() && configDir.isDirectory()) {
            File[] jsonFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles != null) {
                for (File file : jsonFiles) {
                    try (Reader reader = new FileReader(file)) {
                        JsonObject json = GSON.fromJson(reader, JsonObject.class);
                        processJson(json);
                    } catch (Exception e) {
                        VMinus.LOGGER.error("Error reading recipe ban config file '{}':", file.getName(), e);
                    }
                }
            }
        }

         */

        Vminus.LOGGER.info("Loaded {} banned recipes", bannedRecipes.size());
    }

    private void processJson(@NotNull JsonObject obj) {
        if (!obj.has("banned"))
            return;
        JsonArray arr = obj.getAsJsonArray("banned");
        for (JsonElement e : arr) {
            bannedRecipes.add(Identifier.tryParse(e.getAsString()));
        }
    }

    public boolean isBanned(Identifier recipeId) {
        return bannedRecipes.contains(recipeId);
    }

    public Set<Identifier> getBannedRecipes() {
        return Collections.unmodifiableSet(bannedRecipes);
    }
}
