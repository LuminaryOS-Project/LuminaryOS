package me.intel.os.events;

import lombok.Getter;

public class UserEvent {
    @Getter
    private final String username;
    @Getter
    private final long time;
    public UserEvent(String username) {
        this.time = System.currentTimeMillis();
        this.username = username;
    }
}
