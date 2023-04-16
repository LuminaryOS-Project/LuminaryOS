package me.intel.os.communication;

import me.intel.os.Exceptions;
import me.intel.os.plugin.Plugin;

public abstract class Communicator {
   protected Plugin sender;
   protected Plugin receiver;

   public Communicator(Plugin sender, Plugin receiver) throws Exceptions.NotACommunicator {
      if (!(sender instanceof Communicator)) {
         System.out.println("Sender is not a instance of communicator");
         throw new Exceptions.NotACommunicator();
      } else if (!(receiver instanceof Communicator)) {
         System.out.println("Receiver is not a instance of communicator");
         throw new Exceptions.NotACommunicator();
      } else {
         this.sender = sender;
         this.receiver = receiver;
      }
   }

   public void send(String message, Plugin receiver) {
      Communicator c = (Communicator)receiver;
      c.send("Hello!", this.sender);
   }

   public abstract void receive(String var1);
}
