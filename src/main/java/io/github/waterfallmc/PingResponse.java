package io.github.waterfallmc;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.OptionalInt;

@Getter
@RequiredArgsConstructor
final class PingResponse {
    private final OptionalInt protocolVersion;
    private final Optional<String> minecraftVersion;
    @NonNull
    private final String messageOfTheDay;
    private final int currentPlayers;
    private final int maxPlayers;

    PingResponse(String messageOfTheDay, int currentPlayers, int maxPlayers) {
        this(OptionalInt.empty(), Optional.empty(), messageOfTheDay, currentPlayers, maxPlayers);
    }

    PingResponse(int protocolVersion, String minecraftVersion, String messageOfTheDay, int currentPlayers, int maxPlayers) {
        this(OptionalInt.of(protocolVersion), Optional.of(minecraftVersion), messageOfTheDay, currentPlayers, maxPlayers);
    }

}

