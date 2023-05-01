package com.luminary.os;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import com.google.common.eventbus.EventBus;
import com.luminary.os.commands.impl.*;
import com.luminary.os.core.Language;
import com.luminary.os.core.Process;
import com.luminary.os.core.User;
import com.luminary.os.events.AfterShellEvent;
import com.luminary.os.events.BeforeCommandRegisterEvent;
import com.luminary.os.plugin.Plugin;
import com.luminary.os.utils.JSONConfig;
import com.luminary.os.utils.Prompts;
import com.luminary.os.utils.Utils;
import lombok.Getter;

import com.luminary.os.commands.SimpleCommand;
import com.luminary.os.core.*;
import com.luminary.os.core.exceptions.InvalidLanguageException;
import com.luminary.os.core.services.ServiceManager;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.utils.*;
import com.luminary.os.commands.CommandManager;
import com.luminary.os.commands.impl.*;

public class OS {
   @Getter
   private static final String version = "Beta-R1.0";
   @Getter
   private final CommandManager CommandManager = new CommandManager();
   @Getter
   private static String Locale;
   @Getter
   private static com.luminary.os.core.Language Language;
   @Getter
   private final ServiceManager ServiceManager = com.luminary.os.core.services.ServiceManager.getServiceManager();
   public static HashMap<String, Plugin> nameToPlugin = new HashMap<>();
   @Getter
   private static final com.luminary.os.core.ProcessManager ProcessManager = com.luminary.os.core.ProcessManager.getProcessManager();
   @Getter
   private static final JSONConfig config = new JSONConfig("os.json");
   @Getter
   private static OS instance;
   @Getter
   private static final EventBus EventHandler = new EventBus("OS");

   public static String currentDir = System.getProperty("user.dir");
   public static Taskbar programBar = Taskbar.getTaskbar();
   @Getter
   private static User currentUser;

   public Plugin getPlugin(String plugin) {
      return nameToPlugin.getOrDefault(plugin, null);
   }


   private void setUser(User user) {
      currentUser = user;
   }

   public void Start(String[] args) {
      instance = this;
      //
      try {
         if(config.get("locale") != null) {
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
      ProcessManager.start();
      getEventHandler().post(new BeforeCommandRegisterEvent());
      // Commands
      CommandManager.registerCommand(new HelpCommand());
      CommandManager.registerCommand(new LsCommand());
      CommandManager.registerCommand(new RmCommand());
      CommandManager.registerCommand(new CDCommand());
      CommandManager.registerCommand(new ExitCommand());
      CommandManager.registerCommand(new UnameCommand());
      CommandManager.registerCommand(new VerCommand());
      CommandManager.registerCommand(new PWDCommand());
      CommandManager.registerCommand(new KillCommand());
      CommandManager.registerCommand(new SimpleCommand("whoami", "whoami", List.of("me"), PermissionLevel.USER ,(cargs) -> System.out.println(currentUser.getName())));
      CommandManager.registerCommand(new SimpleCommand("lang", "lang", List.of("lang", "language"), PermissionLevel.USER, (cargs) -> {
         System.out.println("Language Pack: " + getLanguage().getName() + " designed for: " + getLanguage().getVersion() + "\n");
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
      try {
         if((boolean) Start.OSoptions.getOrDefault("debug", false) ) {
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
      // saving config
      config.setPref("locale", getLocale());
      config.setMisc("shutdown", System.currentTimeMillis());
      OS.getProcessManager().shutdown();
      nameToPlugin.forEach((k, v) -> v.onDisable());
      config.close();
   }
   public void registerSubscriber(Object o) {
      getEventHandler().register(o);
   }
}
