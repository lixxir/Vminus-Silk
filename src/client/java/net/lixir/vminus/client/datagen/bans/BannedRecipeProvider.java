package net.lixir.vminus.client.datagen.bans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class BannedRecipeProvider implements DataProvider {
    protected final String modId;
    private final DataOutput output;
    private final Set<Identifier> bannedRecipes = new HashSet<>();

    public BannedRecipeProvider(DataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    protected abstract void addBannedRecipes();

    protected void ban(Identifier recipeId) {
        bannedRecipes.add(recipeId);
    }

    protected void ban(String path) {
        ban(Identifier.of(modId, path));
    }

    protected void ban(String namespace, String path) {
        ban(Identifier.of(namespace, path));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull DataWriter cache) {
        addBannedRecipes();

        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        bannedRecipes.forEach(id -> array.add(id.toString()));
        json.add("banned", array);

        Path path = output.getPath()
                .resolve("data/" + modId + "/bans/recipes/banned.json");

        return DataProvider.writeToPath(cache, json, path);
    }

    @Override
    public @NotNull String getName() {
        return "Banned Recipe Provider [" + modId + "]";
    }

    public Set<Identifier> getBannedRecipes() {
        return Collections.unmodifiableSet(bannedRecipes);
    }
}
