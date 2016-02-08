package io.github.waterfallmc;

import lombok.*;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum PingType {
    ANCIENT(AncientPingSender::new),
    v15_14(v15_14PingSender::new),
    v16(v16PingSender::new);



    private final Supplier<AbstractPingSender> factory;

    public AbstractPingSender createSender() {
        return factory.get();
    }

    public static PingType fromVersion(String mcVersion) {
        try {
            switch (Integer.parseInt(mcVersion.split("\\.")[1])) {
                case 6:
                    return v16;
                case 5:
                case 4:
                    return v15_14;
                case 3:
                case 2:
                case 1:
                case 0:
                    return ANCIENT;
                default:
                    throw new IllegalArgumentException("Unknown minecraft version: " + mcVersion);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid minecraft version: " + mcVersion);
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
