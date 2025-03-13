package org.example.loadstaticconfig;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class YamlParserExample {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlParserExample.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (inputStream == null) {
                throw new RuntimeException("YAML file not found!");
            }
            Map<String, Object> config = yaml.load(inputStream);
            System.out.println("Parsed YAML: " + config);

            // 访问 YAML 数据
            Map<String, Object> serverConfig = (Map<String, Object>) config.get("server");
            String host = (String) serverConfig.get("host");
            int port = (Integer) serverConfig.get("port");

            System.out.println("Server Host: " + host);
            System.out.println("Server Port: " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
