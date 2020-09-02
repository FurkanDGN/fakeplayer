package tr.com.infumia.dantero.fakeplayer.nms.v1_8_R2;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.dantero.fakeplayer.api.INPC;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public final class NPC extends EntityPlayer implements INPC {

    private boolean visible = true;

    public NPC(@NotNull final UUID uuid, @NotNull final String name, @NotNull final String tabname,
               @NotNull final CraftWorld world) {
        super(
                ((CraftServer) Bukkit.getServer()).getServer(),
                world.getHandle(),
                new GameProfile(uuid, ChatColor.translateAlternateColorCodes('&', name)),
                new PlayerInteractManager(world.getHandle())
        );

        this.playerInteractManager.b(WorldSettings.EnumGamemode.CREATIVE);
        try (final Socket ignored = new EmptySocket()) {
            final NetworkManager conn = new EmptyNetworkManager(EnumProtocolDirection.CLIENTBOUND);
            this.playerConnection = new EmptyNetHandler(this.server, conn, this);
            conn.a(this.playerConnection);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        Util.addEntityToWorld(this);

        final Player player = this.getBukkitEntity();
        player.setGameMode(GameMode.CREATIVE);
        player.setPlayerListName(tabname);
        Util.addOrRemoveFromPlayerList(this, false);
    }

    @Override
    public void spawn(@NotNull final Location location) {
        Util.addOrRemoveFromPlayerList(this, false);
        Util.sendTabListAdd(this);
        Util.sendPacket(
                new PacketPlayOutNamedEntitySpawn(this)
        );
        Util.setHeadYaw(this, location.getYaw());
        Util.setHeadYaw(this, location.getYaw());
        this.getBukkitEntity().teleport(location);
        Util.sendPositionUpdate(this);
        this.toggleVisible();
        this.toggleVisible();
    }

    @Override
    public void deSpawn() {
        Util.sendPacket(
                new PacketPlayOutEntityDestroy(this.getId()),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this)
        );
        Util.removeFromWorld(this);
        Util.removeFromServerPlayerList(this);
    }

    @Override
    public void toggleVisible() {
        if (this.visible) {
            Util.sendPacket(new PacketPlayOutEntityDestroy(this.getId()));
        } else {
            Util.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        }
        this.visible = !this.visible;
    }

    @Override
    public void die(final DamageSource damagesource) {
        if (!this.dead) {
            super.die(damagesource);
            this.world.removeEntity(this);
        }
    }

    @Override
    public void jump() {
        this.move(0, 1, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("FakePlayer"), () -> {
            this.move(0, -1, 0);
        }, 6);
    }

    @Override
    public void toggleCrouch() {
        setSneaking(!isSneaking());
    }

    @Override
    public void hit() {
        Util.sendPacket(new PacketPlayOutAnimation(this, 0));
    }

    @Override
    public void show(Player player) {
        Util.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
    }

    @Override
    public void hide(Player player) {
        Util.sendPacket(player, new PacketPlayOutEntityDestroy(this.getId()));
    }
}
