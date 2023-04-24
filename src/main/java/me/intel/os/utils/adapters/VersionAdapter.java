package me.intel.os.utils.adapters;

import lombok.AllArgsConstructor;
import me.intel.os.OS;
import me.intel.os.utils.JSONConfig;
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
