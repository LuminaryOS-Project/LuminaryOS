package me.intel.os;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import me.intel.os.commands.SimpleCommand;
import me.intel.os.permissions.PermissionLevel;
import me.intel.os.utils.*;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import me.intel.os.commands.CommandManager;
import me.intel.os.commands.impl.*;
import me.intel.os.core.Color;
import me.intel.os.core.ProcessManager;
import me.intel.os.core.User;
import me.intel.os.plugin.Plugin;

public class OS {
   private final CommandManager CommandManager = new CommandManager();
   public static HashMap<String, Plugin> nameToPlugin = new HashMap<>();
   @Getter
   private static final ProcessManager ProcessManager = me.intel.os.core.ProcessManager.getProcessManager();
   //private static final KeybindListener keybindListener = new KeybindListener();
   @Getter
   private static final JSONConfig config = new JSONConfig("os.json");
   private static OS instance;
   @Getter
   private static final EventBus EventHandler = new EventBus("OS");

   public static String currentDir = System.getProperty("user.dir");
   public static Taskbar programBar = Taskbar.getTaskbar();
   private User currentUser;

   public Plugin getPlugin(String plugin) {
      return nameToPlugin.getOrDefault(plugin, null);
   }

   public CommandManager getCommandManager() {
      return this.CommandManager;
   }

   public void updateUser(User u) {
      this.setUser(u);
   }

   public void updateUser(String name) {
      this.setUser(User.getUserByName(name));
   }

   private void setUser(User user) {
      this.currentUser = user;
   }
   public void Start(String[] args) throws InterruptedException, IOException {
      System.out.println("Initialising IntelOS (Java)");
      instance = this;
      // JVM Things
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         shutdown();
      }));
      ProcessManager.start();
      // Commands
      CommandManager.registerCommand(new HelpCommand());
      CommandManager.registerCommand(new LsCommand());
      CommandManager.registerCommand(new RmCommand());
      CommandManager.registerCommand(new CDCommand());
      CommandManager.registerCommand(new ExitCommand());
      CommandManager.registerCommand(new UnameCommand());
      CommandManager.registerCommand(new VerCommand());
      CommandManager.registerCommand(new PWDCommand());
      CommandManager.registerCommand(new SimpleCommand("whoami", "whoami", List.of("mne"), PermissionLevel.USER ,(cargs) -> { System.out.println(currentUser.getName()); }));
      new Recovery().check(currentDir);
      // Register events
      // END REGISTER EVENTS
      Scanner scanner = new Scanner(System.in);
      System.out.println("Welcome To IntelOS");
      try {
         if((boolean) Start.OSoptions.getOrDefault("debug", false) ) {
            //System.out.println("Running on Debug Mode, OS: " + System.getProperty("os.name") + " Version: " + System.getProperty("os.version")
            //
            //);
            System.out.println("Running on Debug Mode, OS: " + System.getProperty("os.name") +
                    " Build: " + System.getProperty("os.build", "UNKNOWN") +
                    " Compiled on Java 17, Running on " + System.getProperty("java.version")
            );
         }
      } catch (ClassCastException ignored) {}
      while(true) {
         try {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] splitInput = input.split(" ");
            String command = splitInput[0];
            if(!Objects.equals(command, "")){
               String[] arg = new String[splitInput.length - 1];
               System.arraycopy(splitInput, 1, arg, 0, splitInput.length - 1);
               this.CommandManager.executeCommand(command, List.of(arg));
            }
         } catch (NoSuchElementException e) {
            System.exit(0);
         }
      }
   }
   public void shutdown() {
      System.out.println("Shutting Down!");
      System.out.println("Stopping Processes...");
      OS.getProcessManager().shutdown();
      nameToPlugin.forEach((k, v) -> {
         v.onDisable();
      });
      //File f = new File("output.bin");
      config.close();
   }
   public File getConfigFile() throws IOException {
      File config = new File(".config");
      if (!config.exists()) {
         if(config.createNewFile()) {
            var e = 1;
         } else return null;
      }

      return config;
   }


   public static OS getInstance() {
      return instance;
   }

   public User getCurrentUser() {
      return this.currentUser;
   }
}
