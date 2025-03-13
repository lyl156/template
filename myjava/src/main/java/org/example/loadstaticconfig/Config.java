package org.example.loadstaticconfig;

public class Config {
    private ServerConfig server;
    private DatabaseConfig database;

    public ServerConfig getServer() { return server; }
    public void setServer(ServerConfig server) { this.server = server; }

    public DatabaseConfig getDatabase() { return database; }
    public void setDatabase(DatabaseConfig database) { this.database = database; }


    public static class ServerConfig {
        // 如果 class 没有定义为 public, field 为 private 的话，parse 成 yaml 时会出错 !!!!!
        private String host;
        private int port;

        public ServerConfig() {}

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
    }


    public static class DatabaseConfig {
        private String url;
        private String username;
        private String password;

        public DatabaseConfig() {}

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

}
