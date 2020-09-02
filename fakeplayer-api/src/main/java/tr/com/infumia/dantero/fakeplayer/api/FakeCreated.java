package tr.com.infumia.dantero.fakeplayer.api;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface FakeCreated {

    @NotNull
    INPC create(@NotNull String name, @NotNull String tabname, @NotNull World world);

}
