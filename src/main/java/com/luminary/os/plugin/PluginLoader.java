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
import com.luminary.os.OS;
import com.luminary.os.plugin.Plugin;
import com.luminary.os.plugin.PluginDescription;
import com.luminary.os.utils.JSONConfig;
import com.luminary.os.utils.Utils;
import com.luminary.os.utils.versioning.Versioning;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


@RestrictReflect(
        methods = {
                "loadPluginClass",
        }
)
public class PluginLoader {
    public static void load() {
        File pluginFolder = new File("LuminaryOS/plugins");
        File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files != null) {
            for (File file : files) {
                loadPlugin(file);
            }
        }
    }

    public static void loadPlugin(File file) {
        try (JarFile jar = new JarFile(file)) {
            JarEntry pluginConf = jar.getJarEntry("plugin.json");
            if (pluginConf == null) {
                System.out.println("Invalid plugin found at \"" + file.getAbsolutePath() + "\", no plugin.json found!");
                return;
            }

            JSONConfig conf = new JSONConfig(jar.getInputStream(pluginConf));
            String main = conf.getAs("main");
            String targets = conf.getAs("luminary.targetsJava");
            if(targets != null && !targets.isBlank() && !targets.isEmpty()) {
                if(!Versioning.test(targets)) {
                    return;
                }
            }

            try (URLClassLoader pluginLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + file.getAbsolutePath() + "!/")})) {
                loadPluginClass(main, conf, pluginLoader);
            } catch (IOException e) {
                Utils.logErrToFile(e);
            }
        } catch (IOException e) {
            Utils.logErrToFile(e);
        }
    }

    private static void loadPluginClass(String main, JSONConfig conf, URLClassLoader pluginLoader) {
        try {
            Class<?> clz = pluginLoader.loadClass(main);
            if (Plugin.class.isAssignableFrom(clz)) {
                Plugin plugin = (Plugin)
                        clz
                        .getDeclaredConstructor()
                        .newInstance();

                String pluginName = conf.getAs("name");

                plugin.setDescription(new PluginDescription(
                        pluginName,
                        conf.getAs("version"),
                        conf.getAs("description"),
                        conf.getAs("author")
                ));

                if (PluginManager.isRegistered(plugin.getClass())) {
                    System.out.println("A plugin by the name " + pluginName + " is already registered!");
                } else {
                    new Thread(() -> {
                        try {
                            plugin.onEnable();
                            System.out.println("Loaded plugin " + clz.getCanonicalName() + " successfully");
                            PluginManager.registerPlugin(plugin);
                        } catch (Exception e) {
                            plugin.onDisable();
                            Utils.logErrToFile(e);
                        }
                    }, "PLUGIN_LOAD_" + pluginName).start();
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to load plugins main class...");
            Utils.logErrToFile(ex);
        }
    }
}
