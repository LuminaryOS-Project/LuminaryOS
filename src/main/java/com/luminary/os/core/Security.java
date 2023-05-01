package com.luminary.os.core;

import org.mindrot.jbcrypt.BCrypt;

public class Security {
   public static String hashPassword(String password) {
      return BCrypt.hashpw(password, BCrypt.gensalt(14));
   }

   public static String hashPassword(String password, int saltLength) {
      return BCrypt.hashpw(password, BCrypt.gensalt(saltLength));
   }

   public static boolean checkPassword(String password, String hashedPassword) {
      return BCrypt.checkpw(password, hashedPassword);
   }
}
