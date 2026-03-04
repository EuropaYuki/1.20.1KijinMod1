package com.YukiSato.KijinMod.event;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShieldState {

    private static final Map<UUID, Boolean> ACTIVE = new ConcurrentHashMap<>();

    public static boolean isActive(UUID id) {
        return ACTIVE.getOrDefault(id, false);
    }

    public static void setActive(UUID id, boolean active) {
        ACTIVE.put(id, active);
    }
}
