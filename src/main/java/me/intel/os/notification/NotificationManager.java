package me.intel.os.notification;

import java.awt.*;
import java.util.logging.Logger;

public class NotificationManager {
    public static void sendNotification(String title, String text, String tooltip, TrayIcon.MessageType messageType) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip(tooltip);
            tray.add(trayIcon);
            trayIcon.displayMessage(title, tooltip, messageType);
        } catch (Exception e) {
            Logger.getLogger("[OS/Notification Manager] ").warning("Could not send notification");
        }
    }
}