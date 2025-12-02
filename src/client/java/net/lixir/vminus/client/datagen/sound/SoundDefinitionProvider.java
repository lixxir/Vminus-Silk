package net.lixir.vminus.client.datagen.sound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoundDefinitionProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataOutput output;
    private final String modId;

    public SoundDefinitionProvider(DataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull DataWriter writer) {
        JsonObject soundsJson = new JsonObject();

        /*
        for (SoundDefinitionInfo info : VRegistry.fromId(modId).getSoundDefinitionInfo()) {
            soundsJson.add(info.getSoundEventId(), generateSoundEntry(info));
        }


        Path target = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, modId)
                .resolve(new Identifier(modId, "sounds"), ".json");

        try {
            Files.createDirectories(target.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for sounds.json", e);
        }

        return DataProvider.writeToPath(writer, soundsJson, target);

         */
        return null;
    }

    private JsonObject generateSoundEntry(SoundDefinitionInfo info) {
        JsonObject entry = new JsonObject();

        // key
        if (info.getSubtitle() != null) {
            String subtitle = info.getSubtitle().equals("default") ? "subtitles." + info.getPath() : info.getSubtitle();
            entry.addProperty("key", subtitle);
        }

        // sounds array
        JsonArray soundsArray = new JsonArray();
        List<String> paths = info.getPaths();
        int count = info.getCount();
        boolean isOpus = paths != null && !paths.isEmpty() && paths.get(0).endsWith(".opus");

        if (paths != null && !paths.isEmpty()) {
            for (String path : paths) {
                JsonObject soundObj = new JsonObject();
                soundObj.addProperty("name", path);
                soundsArray.add(soundObj);
            }
        } else {
            String base = info.getPath();
            for (int i = 1; i <= count; i++) {
                String file = base + (count > 1 ? i : "") + (isOpus ? ".opus" : ".ogg");
                JsonObject soundObj = new JsonObject();
                soundObj.addProperty("name", modId + ":" + file);
                soundsArray.add(soundObj);
            }
        }

        entry.add("sounds", soundsArray);
        return entry;
    }

    @Override
    public @NotNull String getName() {
        return "SoundDefinitionProvider [" + modId + "]";
    }
}
