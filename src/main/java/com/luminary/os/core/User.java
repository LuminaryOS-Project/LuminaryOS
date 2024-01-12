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

package com.luminary.os.core;

import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.utils.JSONConfig;
import com.luminary.os.utils.Prompts;
import com.luminary.os.utils.Utils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class User {
   @Getter
   private final String name;
   @Getter
   private final PermissionLevel permissionLevel;
   private final JSONConfig usercfg;

   public User(String name, PermissionLevel permissionLevel) {
      this.name = name;
      this.permissionLevel = permissionLevel;
      this.usercfg = new JSONConfig(Paths.get("LuminaryOS", "users", name, "user.json").toString());
   }
   public void setPref(String pref, Object value) {
      usercfg.set("preference." + pref, value);
   }
   public void setSetting(String setting, Object value) {
      usercfg.set("setting." + setting, value);
   }
   public void setMisc(String misc, Object value) {
      usercfg.set("misc." + misc, value);
   }
   public boolean checkPassword(String password) {
      return Security.checkPassword(password, (String) usercfg.get("password"));
   }
   public static @NotNull User createUser(String name, boolean overwrite) {
      Path userPath = Paths.get("LuminaryOS", "users", name);
      Path jsonPath = Paths.get("LuminaryOS", "users", name, "user.json");
      if(Files.exists(userPath) || Files.exists(jsonPath)) {
         if(!overwrite) { throw new IllegalArgumentException("User already exists!"); }
         else { Utils.deleteDirectory(new File(userPath.toString())); }
      }
      Utils.createDirectory(userPath);
      try {
         new File(jsonPath.toString()).createNewFile();
      } catch (IOException ignored) {
         System.out.println("Failed to create user json...");
         System.exit(0);

      }
      try(JSONConfig cfg = new JSONConfig(jsonPath.toString())) {
         cfg.set("permissionlvl", "USER");
         cfg.set("name", name);
         System.out.println("Password must be at least 6 characters, 1 Uppercase and 1 Lowercase");
         cfg.set("password", Prompts.getPassword("Enter password", "^(?=.*[A-Z])(?=.*[a-z]).{6,}$", true));
      }
      return new User(name, PermissionLevel.USER);
   }
   public static User getUserByName(String name) {
      Path userPath = Paths.get("LuminaryOS", "users", name);
      if(Files.exists(userPath)) {
         Path jsonPath = Paths.get("LuminaryOS", "users", name, "user.json");
         if(Files.exists(jsonPath)) {
            try(JSONConfig cfg = new JSONConfig(jsonPath.toString())) {
               PermissionLevel lvl = PermissionLevel.valueOf(cfg.get("permissionlvl").toString());
                return new User(name, lvl);
            }
         }
      }

      return createUser(name, true);
   }
}
