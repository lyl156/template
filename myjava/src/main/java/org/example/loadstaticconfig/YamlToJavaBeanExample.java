package org.example.loadstaticconfig;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Map;

public class YamlToJavaBeanExample {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();

        // 需要将 resources/config.yaml 打包到 target/classes/ 下才可以被 read !!!
        try (InputStream inputStream1 = YamlToJavaBeanExample.class.getClassLoader().getResourceAsStream("config.yaml");
             InputStream inputStream2 = YamlToJavaBeanExample.class.getClassLoader().getResourceAsStream("config.yaml")) {

            if (inputStream1 == null || inputStream2 == null) {
                throw new RuntimeException("YAML file not found!");
            }

            Map<String, Object> config2 = yaml.load(inputStream1);
            System.out.println("Parsed YAML: " + config2);

            Config config = (new Yaml(new Constructor(Config.class))).load(inputStream1);
            if (config == null) {
                throw new RuntimeException("Failed to parse YAML into Config object!");
            }

            System.out.println("Server Host: " + config.getServer().getHost());
            System.out.println("Server Port: " + config.getServer().getPort());
            System.out.println("Database URL: " + config.getDatabase().getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

