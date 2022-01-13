package org.aussie.anarchy.util.config;

import org.aussie.anarchy.Anarchy;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigUtil {
    public ConfigUtil() {
    }

    public static void processConfig(File dataDirectory, String configFile, Class<?> configClass) throws IOException {
        File config = new File(dataDirectory, configFile);
        if (!config.exists()) {
            Files.copy(Objects.requireNonNull(Anarchy.class.getClassLoader().getResourceAsStream(configFile)), config.toPath(), new CopyOption[0]);
        }

        loadConfigFile(dataDirectory, configFile, configClass);
    }

    public static void reloadConfigFile(String configFile, Class<?> config) {
        try {
            File dataDirectory = Anarchy.getPlugin().getDataFolder();
            if (!dataDirectory.exists() && !dataDirectory.mkdir()) {
                return;
            }

            loadConfigFile(dataDirectory, configFile, config);
        } catch (ConfigurateException var3) {
            var3.printStackTrace();
        }

    }

    public static void loadConfigFile(File dataDirectory, String configFile, Class<?> configClass) throws ConfigurateException {
        ConfigurationNode configNode = YamlConfigurationLoader.builder().path((new File(dataDirectory, configFile)).toPath()).build().load();
        Arrays.asList(configClass.getDeclaredFields()).forEach((it) -> {
            try {
                it.setAccessible(true);
                if (List.class.isAssignableFrom(it.getType())) {
                    it.set(configClass, configNode.node(it.getName()).getList(String.class));
                } else {
                    it.set(configClass, configNode.node(new Object[]{it.getName()}).get(it.getType()));
                }
            } catch (IllegalAccessException | SerializationException | SecurityException var11) {
                var11.printStackTrace();
            } catch (IllegalArgumentException var12) {
                String[] text = var12.getMessage().split(" ");
                String value = "";
                String[] packageSplit = text;
                int var8 = text.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String str = packageSplit[var9];
                    if (str.toLowerCase().startsWith(Anarchy.class.getPackage().getName().toLowerCase())) {
                        value = str;
                    }
                }

                packageSplit = value.split("\\.");
                Anarchy.getPlugin().warn(ConfigOutdatedException.getException(packageSplit[packageSplit.length - 1], configFile));
            }

        });
    }
}
