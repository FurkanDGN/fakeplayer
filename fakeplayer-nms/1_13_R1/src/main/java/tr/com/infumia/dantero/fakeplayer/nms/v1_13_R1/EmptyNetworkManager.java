package tr.com.infumia.dantero.fakeplayer.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.EnumProtocolDirection;
import net.minecraft.server.v1_13_R1.NetworkManager;
import net.minecraft.server.v1_13_R1.Packet;

class EmptyNetworkManager extends NetworkManager {

    EmptyNetworkManager(final EnumProtocolDirection flag) {
        super(flag);
        Util.initNetworkManager(this);
    }

    @Override
    public void sendPacket(final Packet<?> packet) {
    }

}