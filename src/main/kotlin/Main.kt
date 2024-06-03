package com.example

import RedisIntegerMap
import java.io.FileInputStream
import java.util.Properties
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

object PropertiesLoader {
    fun loadProperties(filePath: String): Properties {
        val properties = Properties()
        FileInputStream(filePath).use { properties.load(it) }
        return properties
    }
}

data class RedisConfig(
    val host: String,
    val port: Int,
    val maxTotalConnections: Int,
    val mapName: String
) {
    companion object {
        fun fromProperties(properties: Properties): RedisConfig {
            return RedisConfig(
                host = properties.getProperty("redis.host"),
                port = properties.getProperty("redis.port").toInt(),
                maxTotalConnections = properties.getProperty("redis.maxTotalConnections").toInt(),
                mapName = properties.getProperty("redis.mapName")
            )
        }
    }
}

class RedisMapFactory {
    companion object {
        fun createRedisIntegerMap(config: RedisConfig): RedisIntegerMap {
            val poolConfig = JedisPoolConfig().apply {
                maxTotal = config.maxTotalConnections
            }
            val jedisPool = JedisPool(poolConfig, config.host, config.port)
            return RedisIntegerMap(jedisPool, config.mapName)
        }
    }
}

fun main() {
    val properties = PropertiesLoader.loadProperties("src/main/resources/application.properties")
    val config = RedisConfig.fromProperties(properties)

    val redisMap = RedisMapFactory.createRedisIntegerMap(config)

    // Demonstrate put and get
    redisMap.put("key1", 1)
    println("key1: " + redisMap["key1"]) // Output: key1: 1

    // Demonstrate containsKey
    println("Contains key 'key1': " + redisMap.containsKey("key1")) // Output: Contains key 'key1': true

    // Demonstrate containsValue
    println("Contains value 1: " + redisMap.containsValue(1)) // Output: Contains value 1: true

    // Demonstrate putAll
    redisMap.putAll(mapOf("key2" to 2, "key3" to 3))
    println("key2: " + redisMap["key2"]) // Output: key2: 2
    println("key3: " + redisMap["key3"]) // Output: key3: 3

    // Demonstrate size
    println("Size: " + redisMap.size) // Output: Size: 3

    // Demonstrate remove
    redisMap.remove("key1")
    println("key1 after removal: " + redisMap["key1"]) // Output: key1 after removal: null

    // Demonstrate isEmpty
    println("Is empty: " + redisMap.isEmpty()) // Output: Is empty: false

    // Clear the map
    redisMap.clear()
    println("Size after clear: " + redisMap.size) // Output: Size after clear: 0
}
