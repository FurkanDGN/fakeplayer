package tr.com.infumia.dantero.fakeplayer;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.dantero.fakeplayer.commands.FakePlayerCommand;
import tr.com.infumia.dantero.fakeplayer.manager.FakeManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Core extends JavaPlugin {

    private static Core instance;
    private static FakeManager fakemanager;

    private File configfile;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        fakemanager = new FakeManager(config, configfile);
        fakemanager.onLoad();
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new FakePlayerCommand());
        final CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();
        commandCompletions.registerAsyncCompletion("fakeplayers", context ->
                new Mapped<>(
                        Map.Entry::getKey,
                        fakemanager.fakeplayers.entrySet()
                )
        );
    }

    @Override
    public void onDisable() {
        fakemanager.disablePlugin();
    }

    public void loadConfig() {
        configfile = new File(getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            try {
                getDataFolder().mkdir();
                configfile.createNewFile();
            }catch (IOException exception) {
                instance.getLogger().warning("Config file cannot created. Plugin is disabling.");
                exception.printStackTrace();
                Bukkit.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(configfile);

        saveConfigFile();
    }

    @SneakyThrows
    private void saveConfigFile() {
        config.save(configfile);
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigfile() {
        return configfile;
    }

    public static Core getInstance() {
        return instance;
    }

    public FakeManager getFakemanager() {
        return fakemanager;
    }

    @NotNull
    public static FakeManager getFakeManager() {
        if (Core.fakemanager == null) {
            throw new IllegalStateException("You cannot be used FakePlayer plugin before its start!");
        }

        return Core.fakemanager;
    }
}
