package com.luminary.os.permissions;

import com.luminary.os.core.Level;

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
