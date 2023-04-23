package me.intel.os.permissions;

import me.intel.os.core.Level;

public enum PermissionLevel {

   VISIT,
   USER,
   ADMIN,
   ROOT,
   SYSTEM;

   public boolean canPerformAction(PermissionLevel level) {
      return this.ordinal() >= level.ordinal();
   }
   public boolean canPerformAction(Level level) {
      return this.ordinal() >= level.getPermission().ordinal();
   }
}
