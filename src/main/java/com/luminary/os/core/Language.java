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