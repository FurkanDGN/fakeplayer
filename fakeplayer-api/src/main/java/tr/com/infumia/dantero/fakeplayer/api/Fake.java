package tr.com.infumia.dantero.fakeplayer.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Fake {

    @NotNull
    String getName();

    @NotNull
    Location getSpawnPoint();

    void spawn();

    void deSpawn();

    void toggleVisible();

    void jump();

    void toggleCrouch();

    void hit();

    void show(Player player);

    void hide(Player player);
}
