/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.luminary.os.plugin;

import com.luminary.annotations.RestrictReflect;
import com.luminary.os.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RestrictReflect(
        fields = {
                "nameToPlugin",
                "plugins"
        },
        methods = {
                "getPlugin",
                "registerPlugin",
                "getRegisteredPlugins",
                "isRegistered"
        }
)
public class PluginManager {
    private static final Map<String, Plugin> nameToPlugin = new HashMap<>();
    private static final Map<Class<? extends Plugin>, Plugin> plugins = new HashMap<>();

    public static List<Plugin> getRegisteredPlugins() {
        return new ArrayList<>(plugins.values());
    }

    public static void registerPlugin(Plugin plugin) {
        plugins.put(plugin.getClass(), plugin);
        nameToPlugin.put(plugin.getName(), plugin);
    }
    public static void registerPlugin(Class<? extends Plugin> plugin) {
        try {
            Plugin p = plugin.getDeclaredConstructor().newInstance();
            plugins.put(plugin, p);
            nameToPlugin.put(p.getName(), p);
        } catch (Exception ignored) {}
    }
    public static boolean isRegistered(@NotNull Class<? extends Plugin> plugin) {
        return plugins.get(plugin) != null;
    }
    public static @Nullable Plugin getPlugin(@NotNull String plugin) {
        return nameToPlugin.getOrDefault(plugin, null);
    }
    public static @Nullable Plugin getPlugin(@NotNull Class<? extends Plugin> plugin) {
        return plugins.getOrDefault(plugin, null);
    }
    public static @Nullable Class<?> getPluginClass(@NotNull Plugin plugin) {
        Optional<Class<? extends Plugin>> o = plugins.keySet().stream().filter(clazz -> clazz == plugin.getClass()).findFirst();
        return o.orElse(null);
    }
    public static @Nullable Class<?> getPluginClass(String plugin) {
        return Utils.or(nameToPlugin.get(plugin).getClass(), null);
    }
}
