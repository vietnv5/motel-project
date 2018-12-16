package com.slook.util;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by huynx6 on 10/01/2017.
 */
public class DataConfig {

    private static final Logger logger = getLogger(DataConfig.class);

    private static String redisMasterName;
    private static String redisMasterPass;
    private static String redisHost;
    private static int redisPort;
    private static int redisPoolTotal;
    private static String redisSentinelHost;

    static {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

//            redisSentinelHost = resourceBundle.getString("redis_sentinel_host");
//            redisMasterPass = PasswordEncoder.decrypt(resourceBundle.getString("redis_master_pass"));
//            redisMasterName = PasswordEncoder.decrypt(resourceBundle.getString("redis_master_name"));
//            redisPoolTotal = Integer.valueOf(resourceBundle.getString("redisPoolTotal"));

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static String getConfigByKey(String key) {
        return ResourceBundle.getBundle("config").getString(key);
    }

    public static int getRedisPoolTotal() {
        return redisPoolTotal;
    }

    public static void setRedisPoolTotal(int redisPoolTotal) {
        DataConfig.redisPoolTotal = redisPoolTotal;
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static void setRedisHost(String redisHost) {
        DataConfig.redisHost = redisHost;
    }

    public static String getRedisMasterPass() {
        return redisMasterPass;
    }

    public static void setRedisMasterPass(String redisMasterPass) {
        DataConfig.redisMasterPass = redisMasterPass;
    }

    public static String getRedisMasterName() {
        return redisMasterName;
    }

    public static void setRedisMasterName(String redisMasterName) {
        DataConfig.redisMasterName = redisMasterName;
    }

    public static int getRedisPort() {
        return redisPort;
    }

    public static void setRedisPort(int redisPort) {
        DataConfig.redisPort = redisPort;
    }

    public static String getRedisSentinelHost() {
        return redisSentinelHost;
    }

    public static void setRedisSentinelHost(String redisSentinelHost) {
        DataConfig.redisSentinelHost = redisSentinelHost;
    }
}
