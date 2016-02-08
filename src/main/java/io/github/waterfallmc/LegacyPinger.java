package io.github.waterfallmc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.OptionalInt;

public class LegacyPinger {
    public static final int DEFAULT_PORT = 25565;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Insufficient arguments");
            printUsage();
            System.exit(1);
        }
        // NOTE: We parse ping-type last
        InetAddress address;
        try {
            address = InetAddress.getByName(args[1]);
        } catch (UnknownHostException e) {
            System.err.print("Unknown address: ");
            System.err.println(args[1]);
            printUsage();
            System.exit(1);
            throw new AssertionError();
        }
        int port;
        if (args.length > 2) {
            try {
                port = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.print("Invalid port: ");
                System.err.println(args[2]);
                printUsage();
                System.exit(1);
                throw new AssertionError();
            }
        } else {
            port = DEFAULT_PORT;
        }
        if (args[0].equals("all")) {
            for (PingType type : PingType.values()) {
                System.out.print(type.toString());
                System.out.println(" Ping::");
                System.out.println("-----------------------------------------------------------------------------------------");
                doPing(type, address, port);
                System.out.println();
            }
        } else {
            PingType type;
            try {
                type = PingType.fromVersion(args[0]);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                printUsage();
                System.exit(1);
                throw new AssertionError();
            }
            doPing(type, address, port);
        }
    }

    public static void doPing(PingType type, InetAddress address, int port) {
        final AbstractPingSender sender = type.createSender();
        PingResponse response;
        try {
            response = sender.sendPing(address, port);
        } catch (IOException e) {
            System.err.print("Error contacting server: ");
            System.err.println(e.getMessage());
            System.exit(1);
            throw new AssertionError();
        } catch (Throwable t) {
            System.err.println("Unexpected error");
            t.printStackTrace();
            System.exit(1);
            throw new AssertionError();
        }
        System.out.print("Successfully contacted ");
        System.out.print(address.getHostName());
        System.out.print(':');
        System.out.println(port);
        response.getProtocolVersion().ifPresent((protocolVersion) -> {
            System.out.print("Protocol version: ");
            System.out.println(protocolVersion);
        });
        response.getMinecraftVersion().ifPresent((minecraftVersion) -> {
            System.out.print("Minecraft Version: ");
            System.out.println(minecraftVersion);
        });
        System.out.print("Message of the day: ");
        System.out.println(response.getMessageOfTheDay());
        System.out.print("Current Players: ");
        System.out.println(response.getCurrentPlayers());
        System.out.print("Maximum Players: ");
        System.out.println(response.getMaxPlayers());
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar legacyPinger.jar {minecraft version} {hostname} [port]");
    }
}
