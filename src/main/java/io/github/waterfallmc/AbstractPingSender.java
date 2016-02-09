package io.github.waterfallmc;

import lombok.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class AbstractPingSender implements Flushable {

    public synchronized PingResponse sendPing(InetAddress host, int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        try (
                InputStream in = new BufferedInputStream(socket.getInputStream());
                OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            return sendPing(host, port, in, out);
        }
    }

    public synchronized PingResponse sendPing(InetAddress host, int port, InputStream in, OutputStream out) throws IOException {
        setFlushable(out);
        try {
            return sendPing(host, port, (DataInput) new DataInputStream(in), new DataOutputStream(out));
        } finally {
            setFlushable(null);
        }
    }

    protected abstract PingResponse sendPing(InetAddress host, int port, DataInput in, DataOutput out) throws IOException;

    private Flushable flushable;
    protected void setFlushable(Flushable flushable) {
        if (!Thread.holdsLock(this)) throw new IllegalStateException("Not locked");
        if (flushable != null && this.flushable != null) throw new IllegalStateException("Already set");
        this.flushable = flushable;
    }

    public void flush() throws IOException {
        if (!Thread.holdsLock(this)) throw new IllegalStateException("Not locked");
        if (flushable == null) throw new IllegalStateException("Not working");
        flushable.flush();
    }

    public String readLegacyString(DataInput in) throws IOException {
        int length = in.readShort();
        if (length < 0) throw new IOException("Invalid length " + length);
        byte[] stringData = new byte[length * 2];
        in.readFully(stringData);
        return new String(stringData, StandardCharsets.UTF_16BE);
    }

    public void writeLegacyString(DataOutput out, String s) throws IOException {
        byte[] stringData = s.getBytes(StandardCharsets.UTF_16BE);
        out.writeShort(s.length());
        out.write(stringData);
    }
}
