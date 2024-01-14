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

package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.core.Native;
import com.luminary.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;

public class VerCommand extends Command {
   public VerCommand() {
      super("ver", "ver / version", Arrays.asList("ver", "version"));
   }

   public void execute(List<String> args) {


      System.out.println("---------------------------");
      System.out.println("LuminaryOS: " + OS.getLanguage().get("version") + " " + OS.VERSION + " (Build: " + OS.BUILD_NUM + ")");
      if(Native.supportsNative() && OS.getNativeInstance().isPresent()) {
         System.out.println("Native: " + OS.getNativeInstance().get().getInfo());
      }
      System.out.println("Running on: " + System.getProperty("os.name"));
      System.out.println("Language Pack: " + OS.getLanguage().getName() + " designed for: " + OS.getLanguage().getVersion());
      System.out.println("---------------------------");
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
