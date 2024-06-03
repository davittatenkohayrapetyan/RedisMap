import redis.clients.jedis.JedisPool
import java.util.AbstractMap

class RedisIntegerMap(
    private val jedisPool: JedisPool,
    private val mapName: String
) : IRedisIntegerMap {

    override val entries: MutableSet<MutableMap.MutableEntry<String, Int>>
        get() {
            val result = mutableSetOf<MutableMap.MutableEntry<String, Int>>()
            jedisPool.resource.use { jedis ->
                jedis.hgetAll(mapName).forEach { (key, value) ->
                    result.add(AbstractMap.SimpleEntry(key, value.toInt()))
                }
            }
            return result
        }

    override val keys: MutableSet<String>
        get() {
            jedisPool.resource.use { jedis ->
                return jedis.hkeys(mapName)
            }
        }

    override val size: Int
        get() {
            jedisPool.resource.use { jedis ->
                return jedis.hlen(mapName).toInt()
            }
        }

    override val values: MutableCollection<Int>
        get() {
            jedisPool.resource.use { jedis ->
                return jedis.hvals(mapName).map { it.toInt() }.toMutableList()
            }
        }

    override fun clear() {
        jedisPool.resource.use { jedis ->
            jedis.del(mapName)
        }
    }

    override fun containsKey(key: String): Boolean {
        jedisPool.resource.use { jedis ->
            return jedis.hexists(mapName, key)
        }
    }

    override fun containsValue(value: Int): Boolean {
        jedisPool.resource.use { jedis ->
            return jedis.hvals(mapName).contains(value.toString())
        }
    }

    override fun put(key: String, value: Int): Int? {
        jedisPool.resource.use { jedis ->
            val prevValue = jedis.hget(mapName, key)?.toInt()
            jedis.hset(mapName, key, value.toString())
            return prevValue
        }
    }

    override fun putAll(from: Map<out String, Int>) {
        jedisPool.resource.use { jedis ->
            from.forEach { (key, value) ->
                jedis.hset(mapName, key, value.toString())
            }
        }
    }

    override fun remove(key: String): Int? {
        jedisPool.resource.use { jedis ->
            val prevValue = jedis.hget(mapName, key)?.toInt()
            jedis.hdel(mapName, key)
            return prevValue
        }
    }

    override fun get(key: String): Int? {
        jedisPool.resource.use { jedis ->
            return jedis.hget(mapName, key)?.toInt()
        }
    }

    override fun isEmpty(): Boolean {
        jedisPool.resource.use { jedis ->
            return jedis.hlen(mapName) == 0L
        }
    }
}
