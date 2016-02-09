package io.github.waterfallmc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;

public class v16PingSender extends AbstractPingSender {

    public static final String MAGIC_ID = "MC|PingHost";
    public static final int PROTOCOL_VERSION = 74;

    @Override
    public PingResponse sendPing(InetAddress host, int port, DataInput in, DataOutput out) throws IOException {
        // Ping packet
        out.write(0xFE);
        out.write(0x01);
        // Plugin Message
        out.write(0xFA);
        writeLegacyString(out, MAGIC_ID);
        String hostname = host.getHostName();
        out.writeShort(hostname.length() * 2 + 7); // Length of the rest of the data
        out.write(PROTOCOL_VERSION);
        writeLegacyString(out, hostname);
        out.writeInt(port);
        this.flush(); // Flush output
        int packetId = in.readUnsignedByte();
        if (packetId != 0xFF) throw new IOException("Unexpected packet id: " + packetId);
        String response = readLegacyString(in);
        return decodeResponseString(response);
    }

    public static final String MAGIC_HEADER = "§1\u0000";
    public static PingResponse decodeResponseString(String rawData) throws IOException {
        if (!rawData.startsWith(MAGIC_HEADER)) throw new IOException(rawData + "doesn't start with magic header");
        rawData = rawData.substring(MAGIC_HEADER.length()); // Strip header
        // Parse Data
        String[] data = rawData.split("\u0000");
        if (data.length != 5) throw new IOException("Unexpected data " + rawData);
        int protocolVersion;
        try {
            protocolVersion = Integer.parseInt(data[0]);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid protocol version: " + data[0]);
        }
        String minecraftVersion = data[1];
        String messageOfTheDay = data[2];
        int currentPlayers;
        try {
            currentPlayers = Integer.parseInt(data[3]);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number of current players " + data[3]);
        }
        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(data[4]);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number of max players " + data[4]);
        }
        return new PingResponse(protocolVersion, minecraftVersion, messageOfTheDay, currentPlayers, maxPlayers);
    }
}
