package com.inigo.arch.shared.infrastructure;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class UuidUtils {

    private UuidUtils() {
        throw new AssertionError("Not instantiable");
    }

    public static UUID randomUuidV7() {
        return UuidCreator.getTimeOrderedEpoch();
    }

    public static UUID randomUuidV4() {
        return UUID.randomUUID();
    }
}