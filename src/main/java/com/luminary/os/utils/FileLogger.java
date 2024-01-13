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

package com.luminary.os.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private static final String LOG_DIRECTORY = "logs";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy");

    public static void log(String message) {
        String today = DATE_FORMAT.format(new Date());
        String logFileName;
        logFileName = today + ".log";
        String logFilePath = LOG_DIRECTORY + File.separator + logFileName;

        File logFile = new File(logFilePath);
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    //
    public static void init() {
        File logsDirectory = new File(LOG_DIRECTORY);
        if (!logsDirectory.exists()) {
            logsDirectory.mkdir();
        }
    }
}
