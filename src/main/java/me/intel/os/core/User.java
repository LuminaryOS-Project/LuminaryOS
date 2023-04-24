package me.intel.os.core;

import lombok.Getter;
import me.intel.os.permissions.PermissionLevel;
import me.intel.os.utils.JSONConfig;
import me.intel.os.utils.Prompts;
import me.intel.os.utils.Utils;
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
      this.usercfg = new JSONConfig(Paths.get("IntelOS", "users", name, "user.json").toString());
   }
   public boolean checkPassword(String password) {
      return Security.checkPassword(password, (String) usercfg.get("password"));
   }
   public static @NotNull User createUser(String name, boolean overwrite) {
      Path userPath = Paths.get("IntelOS", "users", name);
      Path jsonPath = Paths.get("IntelOS", "users", name, "user.json");
      if(Files.exists(userPath) || Files.exists(jsonPath)) {
         if(!overwrite) { throw new IllegalArgumentException("User already exists!"); }
         if(overwrite) { Utils.deleteDirectory(new File(userPath.toString())); }
      }
      Utils.createDirectory(userPath);
      try {
         new File(jsonPath.toString()).createNewFile();
      } catch (IOException e) {

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
      Path userPath = Paths.get("IntelOS", "users", name);
      if(Files.exists(userPath)) {
         Path jsonPath = Paths.get("IntelOS", "users", name, "user.json");
         if(Files.exists(jsonPath)) {
            try(JSONConfig cfg = new JSONConfig(jsonPath.toString())) {
               PermissionLevel lvl = PermissionLevel.valueOf(cfg.get("permissionlvl").toString());
               if(lvl != null) {
                  return new User(name, lvl);
               }
            }
         }
      }
      return createUser(name, true);
   }
}
