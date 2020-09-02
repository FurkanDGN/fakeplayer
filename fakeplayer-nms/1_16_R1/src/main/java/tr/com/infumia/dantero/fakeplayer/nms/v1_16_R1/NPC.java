package tr.com.infumia.dantero.fakeplayer.nms.v1_16_R1;

import com.mojang.authlib.GameProfile;
import org.bukkit.util.Vector;
import tr.com.infumia.dantero.fakeplayer.api.INPC;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.UUID;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        this.playerInteractManager.b(EnumGamemode.CREATIVE);
        try (final Socket ignored = new EmptySocket()) {
            final NetworkManager conn = new EmptyNetworkManager(EnumProtocolDirection.CLIENTBOUND);
            this.playerConnection = new EmptyNetHandler(this.server, conn, this);
            conn.setPacketListener(this.playerConnection);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        Util.addEntityToWorld(this);
        final Player player = this.getBukkitEntity();
        player.setGameMode(GameMode.CREATIVE);
        player.setPlayerListName(tabname);
        player.setSleepingIgnored(true);
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
            ((WorldServer) this.world).removeEntity(this);
        }
    }

    @Override
    public void jump() {
        this.move(EnumMoveType.PLAYER, new Vec3D(0, 1, 0));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("FakePlayer"), () -> {
            this.move(EnumMoveType.PLAYER, new Vec3D(0, -1, 0));
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
        player.showPlayer(this.getBukkitEntity());
    }

    @Override
    public void hide(Player player) {
        player.hidePlayer(this.getBukkitEntity());
    }
}