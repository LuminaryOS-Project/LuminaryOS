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

package com.luminary.os;

import com.google.common.eventbus.EventBus;
import com.luminary.annotations.RestrictReflect;
import com.luminary.os.commands.CommandManager;
import com.luminary.os.commands.SimpleCommand;
import com.luminary.os.commands.impl.*;
import com.luminary.os.core.*;
import com.luminary.os.core.exceptions.InvalidLanguageException;
import com.luminary.os.core.services.ServiceManager;
import com.luminary.os.events.AfterShellEvent;
import com.luminary.os.events.BeforeCommandRegisterEvent;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.plugin.Plugin;
import com.luminary.os.plugin.PluginManager;
import com.luminary.os.utils.JSONConfig;
import com.luminary.os.utils.Prompts;
import com.luminary.os.utils.Utils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;


@RestrictReflect(
        fields = {
                "instance",
                "mainThread",
                "NATIVE",
                "VERSION",
                "BUILD_NUM",
                "CommandManager",
                "EventHandler",
                "config",
                "ProcessManager",
                "nameToPlugin",
                "plugins",
                "ServiceManager",
                "Language"
        },
        methods = {
                "example",
                "initOS",
                "start",
                "shutdown"
        }
)
public final class OS {
   private final Thread mainThread;
   @Getter
   private static Optional<Native> NativeInstance = Optional.empty();
   public static final String VERSION = "v1_R3";
   public static final int BUILD_NUM = 240113;
   @Getter
   private final CommandManager commandManager = CommandManager.getInstance();
   @Getter
   private static String Locale;
   @Getter
   private static com.luminary.os.core.Language Language;
   private final ServiceManager serviceManager = ServiceManager.getServiceManager();
   private static final Map<String, Plugin> nameToPlugin = new HashMap<>();
   private static final Map<Class<? extends Plugin>, Plugin> plugins = new HashMap<>();
   @Getter
   private static final ProcessManager processManager = ProcessManager.getProcessManager();
   private static final JSONConfig config = new JSONConfig("os.json");
   @Getter
   private static OS instance;
   @Getter
   private static final EventBus EventHandler = new EventBus("OS");
   public static String currentDir = System.getProperty("user.dir");
   public static Taskbar programBar = null;
   @Getter
   private static User currentUser;
   // Plugin related
   // End of plugin stuff
   private void setUser(User user) {
      currentUser = user;
   }
   private OS() {
      this.mainThread = Thread.currentThread();
   }

   static void initOS(String[] args) {
      if(instance == null) {
         new OS().start(args);
      }
      throw new IllegalStateException("OS is already initialized!");
   }

   private static void example() {
      System.out.println("Example Invoked");
   }

   private void start(String[] args) {
      instance = this;
      if (Native.supportsNative()) {
         NativeInstance = Optional.of(Native.getInstance());
      }
      if(Native.isWindows()) {
         programBar = Taskbar.getTaskbar();
      }
      //
      try {
         if (config.get("locale") != null) {
            Locale = (String) config.get("locale");
         } else {
            Locale = java.util.Locale.getDefault().getLanguage();
         }
         Language = new Language(Locale);
      } catch (InvalidLanguageException e) {
         Locale = "en";
         System.out.println("Couldn't find default language, defaulting to english.");
         Language = new Language("en");
      }
      //
      System.out.println(getLanguage().get("initialising") + " LuminaryOS (Java)\n");
      // JVM Things
      Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
      processManager.start();

      getEventHandler().post(new BeforeCommandRegisterEvent());
      // Commands
      commandManager.registerCommands(
              new HelpCommand(),
              new InstallCommand(),
              new LsCommand(),
              new RmCommand(),
              new CDCommand(),
              new ExitCommand(),
              new UnameCommand(),
              new VerCommand(),
              new PWDCommand(),
              new KillCommand()
      );
      //
      commandManager.registerCommand(new SimpleCommand("whoami", "whoami", List.of("me"), PermissionLevel.USER ,(cargs) -> System.out.println(currentUser.getName())));
      commandManager.registerCommand(new SimpleCommand("lang", "lang", List.of("lang", "language"), PermissionLevel.USER, (cargs) -> System.out.println("Language Pack: " + getLanguage().getName() + " designed for: " + getLanguage().getVersion() + "\n")));
      //
      commandManager.registerCommand(new SimpleCommand("plist", "plist", List.of("plist"), PermissionLevel.USER, (cargs) -> {
         System.out.println("Registered Plugins:");
         PluginManager.getRegisteredPlugins().forEach(pl -> {
            System.out.println("\tClass: " + pl.getClass().getCanonicalName());
            System.out.println(pl.getDescription().toString());
         });
      }));
      commandManager.registerCommand(new SimpleCommand("screensaver", "Displays a screensaver", List.of("scrnsvr", "srcvr"), PermissionLevel.USER, (cargs) -> {
         Screensaver ss = new Screensaver("donut");
         try { ss.start(); } catch (Exception ignored) {}
      }));
      new Recovery().check(currentDir);
      //
      List<String> users = Arrays.stream(new File("LuminaryOS/users").list())
              .filter(name -> new File("LuminaryOS/users", name).isDirectory())
              .filter(name -> List.of(new File("LuminaryOS/users", name).list()).contains("user.json"))
              .toList();
      Scanner scanner = new Scanner(System.in);
      System.out.println("Users: " + String.join(",", users));
      System.out.println("Select a user to login to!" + "\n");
      String selectedUser;
      while(true) {
         System.out.print("Username: ");
         String user = scanner.nextLine();
         if(users.contains(user)) {
            selectedUser = user;
            break;
         }
      }
      boolean loggedIn = false;
      while(!loggedIn) {
         assert System.console() != null;
         loggedIn = User.getUserByName(selectedUser).checkPassword(Prompts.getPassword("Enter password", "^(?=.*[A-Z])(?=.*[a-z]).{6,}$", false));
         if(loggedIn) {
            System.out.println("Logged in");
         } else {
            System.out.println("Incorrect password");
         }
      }
      setUser(User.getUserByName(selectedUser));
      Utils.clearScreen();
      //
      System.out.println(getLanguage().get("welcome") + " LuminaryOS");
      getEventHandler().post(new AfterShellEvent());
      if((boolean) Start.OSoptions.getOrDefault("debug", false) ) {
         System.out.println("Running on Debug Mode, OS: " + System.getProperty("os.name") +
                 " Build: " + System.getProperty("os.build", "UNKNOWN") +
                 " Compiled on Java 17, Running on " + System.getProperty("java.version")
         );
      }
      while(true) {
         try {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] splitInput = input.split(" ");
            String command = splitInput[0];
            if(!Objects.equals(command, "")){
               String[] arg = new String[splitInput.length - 1];
               System.arraycopy(splitInput, 1, arg, 0, splitInput.length - 1);
               this.commandManager.executeCommand(command, List.of(arg));
            }
         } catch (Exception e) {

            System.exit(0);
         }
      }
   }
   public void shutdown() {
      System.out.println("Shutting Down!");
      System.out.println("Stopping Processes...");
      // saving config
      config.setPref("locale", getLocale());
      config.setMisc("shutdown", System.currentTimeMillis());
      OS.getProcessManager().shutdown();
      nameToPlugin.values().forEach(Plugin::onDisable);
      config.close();
   }
}
