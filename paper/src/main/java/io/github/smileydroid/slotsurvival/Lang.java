package io.github.smileydroid.slotsurvival;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * Traductions côté serveur : chaque joueur reçoit les messages dans la langue de son client Minecraft.
 * Les fichiers lang/xx_yy.json (les mêmes que ceux du mod Fabric) sont lus dans le jar du plugin.
 * Langue de repli : anglais (en_us).
 */
final class Lang {

    private static final String FALLBACK = "en_us";

    private final Map<String, Map<String, String>> bundles = new HashMap<>();

    Lang(File pluginJar, Logger logger) {
        try (JarFile jar = new JarFile(pluginJar)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith("lang/") || !name.endsWith(".json")) {
                    continue;
                }
                String code = name.substring("lang/".length(), name.length() - ".json".length());
                try (Reader reader = new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8)) {
                    JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                    Map<String, String> map = new HashMap<>();
                    for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                        map.put(e.getKey(), e.getValue().getAsString());
                    }
                    bundles.put(code, map);
                } catch (Exception error) {
                    logger.warning("Fichier de langue illisible : " + name + " (" + error + ")");
                }
            }
        } catch (Exception error) {
            logger.warning("Impossible de lire les langues : " + error);
        }
        logger.info(bundles.size() + " fichiers de langue chargés.");
    }

    /** Texte traduit pour une locale (anglais si la langue ou la clé est absente). */
    String get(Locale locale, String key) {
        String value = bundleFor(locale).get(key);
        if (value == null) {
            value = bundles.getOrDefault(FALLBACK, Map.of()).get(key);
        }
        return value == null ? key : value;
    }

    private Map<String, String> bundleFor(Locale locale) {
        Map<String, String> english = bundles.getOrDefault(FALLBACK, Map.of());
        if (locale == null) {
            return english;
        }
        String language = locale.getLanguage().toLowerCase(Locale.ROOT);
        String country = locale.getCountry().toLowerCase(Locale.ROOT);
        Map<String, String> found = bundles.get(language + "_" + country);
        if (found == null) {
            found = bundles.get(language + "_" + language);
        }
        if (found == null) {
            for (Map.Entry<String, Map<String, String>> entry : bundles.entrySet()) {
                if (entry.getKey().startsWith(language + "_")) {
                    found = entry.getValue();
                    break;
                }
            }
        }
        return found == null ? english : found;
    }
}
