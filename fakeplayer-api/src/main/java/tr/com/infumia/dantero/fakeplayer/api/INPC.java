package tr.com.infumia.dantero.fakeplayer.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface INPC {

    void spawn(@NotNull Location location);

    void deSpawn();

    void toggleVisible();

    void jump();

    void toggleCrouch();

    void hit();

    void show(Player player);

    void hide(Player player);
}
