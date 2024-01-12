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
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.utils.Log;
import com.luminary.os.utils.Prompts;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RmCommand extends Command {
   public RmCommand() {
      super("rm", OS.getLanguage().get("rmUsage"), Arrays.asList("remove", "rem"));
   }

   public void execute(List<String> args) {
      if(!args.isEmpty() && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
         return;
      }
      File f = new File(args.get(0));
      if (!f.exists()) {
         Log.info("[!] File does not exist");
      } else {
         if(Prompts.YNPrompt()) { f.delete(); } else { return; }
         Log.info("[-] File removed.");
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
