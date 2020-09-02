package tr.com.infumia.dantero.fakeplayer.manager;

import io.github.portlek.bukkitlocation.LocationUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.dantero.fakeplayer.Core;
import tr.com.infumia.dantero.fakeplayer.api.Fake;
import tr.com.infumia.dantero.fakeplayer.handle.FakeBasic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class FakeManager {

    public final Map<String, Fake> fakeplayers = new HashMap<>();
    private final FileConfiguration configuration;
    private final File file;

    public void onLoad() {
        this.configuration.getKeys(false).stream()
                .map(name ->
                        new FakeBasic(
                                name,
                                LocationUtil.fromKey(this.configuration.getString(name, ""))
                                        .orElseThrow(() ->
                                                new RuntimeException("Location couldn't parse!"))
                        ))
                .forEach(fake -> {
                    fake.spawn();
                    this.fakeplayers.put(fake.getName(), fake);
                });
    }

    @SneakyThrows
    public Fake addFake(@NotNull final String name, @NotNull final Location location) {
        final Fake fake = new FakeBasic(name, location);
        fake.spawn();
        this.fakeplayers.put(name, fake);
        this.configuration.set(name, LocationUtil.toKey(location));
        this.configuration.save(file);
        return fake;
    }

    public void removeFake(@NotNull final String name) {
        Optional.ofNullable(this.fakeplayers.remove(name)).ifPresent(fake -> {
            fake.deSpawn();
            this.configuration.set(name, null);
            try {
                this.configuration.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void disablePlugin() {
        fakeplayers.values().forEach(Fake::deSpawn);
    }
}
