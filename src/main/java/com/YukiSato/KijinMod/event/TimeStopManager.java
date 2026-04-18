package com.YukiSato.KijinMod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeStopManager {

    public static class TimeStopState {

        private final UUID owner;
        private final long endGameTime;

        public TimeStopState(UUID owner, long endGameTime) {
            this.owner = owner;
            this.endGameTime = endGameTime;
        }

        public UUID getOwner() {
            return owner;
        }

        public long getEndGameTime() {
            return endGameTime;
        }
    }

    private static final Map<ServerLevel, TimeStopState> ACTIVE = new HashMap<>();

    public static void start(ServerLevel level, Player owner, int durationTicks) {
        ACTIVE.put(level, new TimeStopState(owner.getUUID(), level.getGameTime() + durationTicks));
    }

    public static boolean isStopped(ServerLevel level) {
        TimeStopState state = ACTIVE.get(level);
        if (state == null) return false;

        if (level.getGameTime() >= state.getEndGameTime()) {
            ACTIVE.remove(level);
            return false;
        }

        return true;
    }

    public static boolean isOwner(ServerLevel level, UUID uuid) {
        TimeStopState state = ACTIVE.get(level);
        return state != null && state.getOwner().equals(uuid);
    }

    public static void stop(ServerLevel level) {
        ACTIVE.remove(level);
    }
}
