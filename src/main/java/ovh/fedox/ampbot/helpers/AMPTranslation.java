package ovh.fedox.ampbot.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AMPTranslation {
    private static final Logger LOGGER = Logger.getLogger(AMPTranslation.class.getName());
    private static final String DEFAULT_LANG = "en-US";
    private static final String LANG_DIR = "languages";

    private final Map<String, JsonObject> translationsMap;
    private final Map<String, String> languageCodeMap;

    public AMPTranslation() {
        this.translationsMap = new HashMap<>();
        this.languageCodeMap = new HashMap<>();
        loadAllTranslations();
    }

    private void loadAllTranslations() {
        try {
            Files.list(Paths.get(LANG_DIR))
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(this::loadTranslationFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error listing language files", e);
        }
    }

    private void loadTranslationFile(Path langFile) {
        try {
            String content = Files.readString(langFile);
            Gson gson = new Gson();
            JsonObject translations = gson.fromJson(content, JsonObject.class);
            String fileNameLangCode = langFile.getFileName().toString().replace(".json", "");

            String jsonLangCode = translations.getAsJsonObject("language").get("code").getAsString();

            translationsMap.put(jsonLangCode, translations);
            languageCodeMap.put(fileNameLangCode, jsonLangCode);
            languageCodeMap.put(jsonLangCode, jsonLangCode); // Also map the code to itself

            LOGGER.info("Loaded translations for language: " + jsonLangCode + " (file: " + fileNameLangCode + ")");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading language file: " + langFile, e);
        }
    }

    public String getTranslation(String key, String langCode) {
        String actualLangCode = languageCodeMap.getOrDefault(langCode, DEFAULT_LANG);
        JsonObject translations = translationsMap.get(actualLangCode);

        if (translations == null) {
            LOGGER.warning("No translations available for language: " + langCode + " (mapped to: " + actualLangCode + ")");
            return key;
        }

        String[] keys = key.split("\\.");
        JsonElement element = translations;

        for (String k : keys) {
            if (element instanceof JsonObject) {
                element = ((JsonObject) element).get(k);
            } else {
                LOGGER.warning("Translation not found for key: " + key + " in language: " + actualLangCode);
                return key;
            }
        }

        if (element != null && element.isJsonPrimitive()) {
            return element.getAsString();
        } else {
            LOGGER.warning("Translation not found or is not a string for key: " + key + " in language: " + actualLangCode);
            return key;
        }
    }

    public String[] getAvailableLanguages() {
        return translationsMap.keySet().toArray(new String[0]);
    }
}