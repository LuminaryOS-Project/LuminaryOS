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

import com.luminary.os.utils.JSONConfig;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;

public abstract class Plugin {
   @Getter
   @Setter
   private PluginDescription description;
   public @NotNull File getFolder() {
      if(description.getName() == null) {
         throw new NullPointerException("Plugin name is null, please specify a name in the plugin.json");
      }
      File folder = Paths.get("LuminaryOS","plugins", description.getName(), "/").toFile();

      if (!folder.exists()) {
         folder.mkdirs();
      }

      return folder;
   }
   //

   public Plugin() {
      this.description = null;
   }
   //
   @NotNull
   public JSONConfig getConfig() {
      return new JSONConfig(Paths.get(getFolder().getAbsolutePath(), "config.json").toString());
   }

   public abstract void onEnable();

   public abstract void onDisable();

   public String getName() {
      return getClass().getName();
   }
}
