package me.intel.os;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import me.intel.os.commands.CommandManager;
import me.intel.os.commands.impl.*;
import me.intel.os.core.ProcessManager;
import me.intel.os.core.User;
import me.intel.os.plugin.Plugin;

public class OS {
   private final CommandManager CommandManager = new CommandManager();
   public static HashMap<String, Plugin> nameToPlugin = new HashMap<>();
   private static final HashMap<String, Object> config = new HashMap<>();
   @Getter
   private static final ProcessManager ProcessManager = me.intel.os.core.ProcessManager.getProcessManager();
   private static final Logger logger = Logger.getLogger("[OS]");
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

      //programBar.setProgressValue(50);
      //NotificationManager.sendNotification("OS Startup", "IntelOS has successfully started up", "Successful_startup", TrayIcon.MessageType.INFO);
      System.out.println("Initialising IntelOS (Java)");
      //config.put("dev.enabled", false);
      // logger.info(JSON.readJSONFromFile("os.json").get("data").toString());
      // logger.info("Test!");
      instance = this;
      CommandManager.registerCommand(new HelpCommand());
      CommandManager.registerCommand(new LsCommand());
      CommandManager.registerCommand(new RmCommand());
      CommandManager.registerCommand(new CDCommand());
      CommandManager.registerCommand(new ExitCommand());
      Scanner scanner = new Scanner(System.in);
      System.out.println("Welcome To IntelOS");
      //

      //programBar.setProgressValue(100);
      while(true) {
         try {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] splitInput = input.split(" ");
            String command = splitInput[0];
            String[] arg = new String[splitInput.length - 1];
            System.arraycopy(splitInput, 1, arg, 0, splitInput.length - 1);
            this.CommandManager.executeCommand(command, arg);
         } catch (NoSuchElementException e) {
            System.out.println("Detected Ctrl + C, Shutting Down!");
            System.exit(0);
         }
      }
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

   public static Map getConfig() {
      return config;
   }

   public static OS getInstance() {
      return instance;
   }

   public User getCurrentUser() {
      return this.currentUser;
   }
}
