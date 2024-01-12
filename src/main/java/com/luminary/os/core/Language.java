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

package com.luminary.os.core;

import com.luminary.os.core.exceptions.InvalidLanguageException;
import com.luminary.os.utils.JSONConfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Language {
    private final JSONConfig cfg;
    public Language(String language) throws InvalidLanguageException {
        Path LPath = Paths.get("LuminaryOS", "langs", language + ".json");
        if(!new File(LPath.toString()).exists() || new File(LPath.toString()).length() == 0) {
            throw new InvalidLanguageException("Specified Language does not exist.");
        } else {
            cfg = new JSONConfig(LPath.toString());
        }
    }
    public String get(String path)  {
        Object translated = cfg.get("definitions." + path);
        try {
            return (String) translated;
        } catch (ClassCastException e) {
            return null;
        }
    }
    public String getVersion() {
        return (String) cfg.get("designedFor");
    }
    public String getName() {
        return (String) cfg.get("lang");
    }
}