package com.example.pokemonkotlindemo.base.network.request;

public class AppConfig {
    private static AppConfig CONFIG;

    private String env;

    /**
     * 初始化
     * @param env 参考meta_hosts
     */
    public static void init(String env) {
        if (CONFIG == null) {
            CONFIG = new AppConfig();
        }
        CONFIG.env = env;
    }

    private AppConfig() {

    }

    public static boolean isDebug() {
        // 默认是测试环境
        return "debug".equals(CONFIG.env);
    }

    public static boolean isProd() {
        // 默认是正式环境
        return "release".equals(CONFIG.env);
    }

    public static String getEnv() {
        return CONFIG.env;
    }

}
