package com.luminary.os.events;

import lombok.Getter;

@Getter
public class UserEvent {
    private final String username;
    private final long time;
    public UserEvent(String username) {
        this.time = System.currentTimeMillis();
        this.username = username;
    }
}
