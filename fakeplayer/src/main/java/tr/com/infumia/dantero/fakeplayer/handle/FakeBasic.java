package tr.com.infumia.dantero.fakeplayer.handle;

import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.versionmatched.VersionMatched;
import org.bukkit.entity.Player;
import tr.com.infumia.dantero.fakeplayer.api.Fake;
import tr.com.infumia.dantero.fakeplayer.api.FakeCreated;
import tr.com.infumia.dantero.fakeplayer.api.INPC;
import tr.com.infumia.dantero.fakeplayer.nms.v1_10_R1.FakeCreated1_10_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_11_R1.FakeCreated1_11_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_12_R1.FakeCreated1_12_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_13_R1.FakeCreated1_13_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_13_R2.FakeCreated1_13_R2;
import tr.com.infumia.dantero.fakeplayer.nms.v1_14_R1.FakeCreated1_14_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_15_R1.FakeCreated1_15_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_16_R1.FakeCreated1_16_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_8_R1.FakeCreated1_8_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_8_R2.FakeCreated1_8_R2;
import tr.com.infumia.dantero.fakeplayer.nms.v1_8_R3.FakeCreated1_8_R3;
import tr.com.infumia.dantero.fakeplayer.nms.v1_9_R1.FakeCreated1_9_R1;
import tr.com.infumia.dantero.fakeplayer.nms.v1_9_R2.FakeCreated1_9_R2;
import java.util.Optional;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeBasic implements Fake {

    private static final FakeCreated FAKE_CREATED = new VersionMatched<>(
            FakeCreated1_16_R1.class,
            FakeCreated1_15_R1.class,
            FakeCreated1_14_R1.class,
            FakeCreated1_13_R2.class,
            FakeCreated1_13_R1.class,
            FakeCreated1_12_R1.class,
            FakeCreated1_11_R1.class,
            FakeCreated1_10_R1.class,
            FakeCreated1_9_R2.class,
            FakeCreated1_9_R1.class,
            FakeCreated1_8_R3.class,
            FakeCreated1_8_R2.class,
            FakeCreated1_8_R1.class).of()
            .create()
            .orElseThrow(() ->
                    new RuntimeException("Somethings were wrong!"));

    @NotNull
    private final String name;

    @NotNull
    private final Location spawnpoint;

    @Nullable
    private INPC npc;

    public FakeBasic(@NotNull final String name, @NotNull final Location spawnpoint) {
        this.name = name;
        this.spawnpoint = spawnpoint;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public Location getSpawnPoint() {
        return this.spawnpoint;
    }

    @Override
    public void spawn() {
        Optional.ofNullable(this.spawnpoint.getWorld()).ifPresent(world -> {
            if (!Optional.ofNullable(this.npc).isPresent()) {
                this.npc = FakeBasic.FAKE_CREATED.create(
                        this.name,
                        "[FP] " + this.name,
                        world);
            }
            this.npc.spawn(this.spawnpoint);
        });
    }

    @Override
    public void deSpawn() {
        Optional.ofNullable(this.npc).ifPresent(INPC::deSpawn);
    }

    @Override
    public void toggleVisible() {
        Optional.ofNullable(this.npc).ifPresent(INPC::toggleVisible);
    }

    @Override
    public void jump() {
        Optional.ofNullable(this.npc).ifPresent(INPC::jump);
    }

    @Override
    public void toggleCrouch() {
        Optional.ofNullable(this.npc).ifPresent(INPC::toggleCrouch);
    }

    @Override
    public void hit() {
        Optional.ofNullable(this.npc).ifPresent(INPC::hit);
    }

    @Override
    public void show(Player player) {
        Optional.ofNullable(this.npc).ifPresent(inpc -> inpc.show(player));
    }

    @Override
    public void hide(Player player) {
        Optional.ofNullable(this.npc).ifPresent(inpc -> inpc.hide(player));
    }
}
