package io.github.waterfallmc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;

public class AncientPingSender extends AbstractPingSender {
    @Override
    public PingResponse sendPing(InetAddress host, int port, DataInput in, DataOutput out) throws IOException {
        out.write(0xFE);
        this.flush(); // Flush output
        int packetId = in.readUnsignedByte();
        if (packetId != 0xFF) throw new IOException("Unexpected packet id " + packetId);
        String rawData = readLegacyString(in);
        String[] data = rawData.split("§");
        if (data.length != 3) throw new IOException("Invalid data " + rawData);
        String motd = data[0];
        int currentPlayerCount;
        try {
            currentPlayerCount = Integer.parseInt(data[1]);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid player count " + data[1]);
        }
        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(data[2]);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid max players " + data[1]);
        }
        return new PingResponse(motd, currentPlayerCount, maxPlayers);
    }
}
