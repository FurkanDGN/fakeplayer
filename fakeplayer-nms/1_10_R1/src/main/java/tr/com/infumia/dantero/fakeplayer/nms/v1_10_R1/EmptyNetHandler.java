package tr.com.infumia.dantero.fakeplayer.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.*;

class EmptyNetHandler extends PlayerConnection {

    EmptyNetHandler(final MinecraftServer minecraftServer, final NetworkManager networkManager,
                    final EntityPlayer entityPlayer) {
        super(minecraftServer, networkManager, entityPlayer);
    }

    @Override
    public void sendPacket(final Packet packet) {
    }

}