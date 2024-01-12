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

package com.luminary.os.notification;

import com.luminary.os.OS;

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
            Logger.getLogger("[OS/Notification Manager] ").warning(OS.getLanguage().get("couldNotSendNotif"));
        }
    }
}
