package com.luminary.os.utils.adapters;

import lombok.AllArgsConstructor;
import com.luminary.os.utils.JSONConfig;
import static org.jetbrains.annotations.ApiStatus.Experimental;
@AllArgsConstructor
@Experimental
public class VersionAdapter extends Adapter {
    private String version;
    private JSONConfig cfg;

    @Override
    public void handle() {
    }
}
