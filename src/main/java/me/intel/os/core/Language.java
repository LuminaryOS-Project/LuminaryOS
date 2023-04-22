package me.intel.os.core;

import me.intel.os.core.exceptions.InvalidLanguageException;
import me.intel.os.utils.JSONConfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Language {
    JSONConfig cfg;
    public Language(String language) throws InvalidLanguageException {
        Path LPath = Paths.get("IntelOS", "langs", language + ".json");
        if(!new File(LPath.toString()).exists() || new File(LPath.toString()).length() == 0) {
            throw new InvalidLanguageException("Specified Language does not exist.");
        } else {
            cfg = new JSONConfig(LPath.toString());
        }
    }
    public String get(String path, String defaultvalue)  {
        Object translated = cfg.get(path);
        try {
            return (String) translated;
        } catch (ClassCastException e) {
            return defaultvalue;
        }
    }
}
