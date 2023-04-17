package me.intel.os.core;

import me.intel.os.events.LoginEvent;
import me.intel.os.permissions.PermissionLevel;

public class User {
   public String name;
   public PermissionLevel permissionLevel;
   public String location;

   public User(String name, PermissionLevel permissionLevel, String location) {
      this.location = location;
      this.name = name;
      this.permissionLevel = permissionLevel;
   }

   public PermissionLevel getPermissionLevel() {
      return this.permissionLevel;
   }

   public String getLocation() {
      return this.location;
   }

   public String getName() {
      return this.name;
   }

   public static User getUserByName(String name) {
      return new User(name, PermissionLevel.USER, "/users/" + name);
   }
}
