package io.github.waterfallmc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;

public class v15_14PingSender extends AbstractPingSender {
    @Override
    public PingResponse sendPing(InetAddress host, int port, DataInput in, DataOutput out) throws IOException {
        out.write(0xFE);
        out.write(0x01);
        this.flush(); // Flush output
        int packetId = in.readUnsignedByte();
        if (packetId != 0xFF) throw new IOException("Unexpected packet id: " + packetId);
        String responseString = readLegacyString(in);
        return v16PingSender.decodeResponseString(responseString);
    }
}
