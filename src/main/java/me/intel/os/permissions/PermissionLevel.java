package me.intel.os.permissions;

public enum PermissionLevel {
   SYSTEM,
   ROOT,
   ADMIN,
   USER,
   VISIT;

   // $FF: synthetic method
   private static PermissionLevel[] $values() {
      return new PermissionLevel[]{SYSTEM, ROOT, ADMIN, USER, VISIT};
   }
}
