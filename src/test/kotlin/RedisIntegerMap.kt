import org.junit.After
import org.junit.Before
import org.junit.Test
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RedisIntegerMapTest {

    private lateinit var jedisPool: JedisPool
    private lateinit var redisMap: RedisIntegerMap

    @Before
    fun setUp() {
        // Assume Redis is running on localhost:6379
        jedisPool = JedisPool(JedisPoolConfig(), "localhost", 6379)
        redisMap = RedisIntegerMap(jedisPool, "testMap")

        // Clear the Redis database to ensure a clean state for each test
        jedisPool.resource.use { it.flushDB() }
    }

    @After
    fun tearDown() {
        jedisPool.close()
    }

    @Test
    fun testPutAndGet() {
        assertNull(redisMap.put("key1", 1))
        assertEquals(1, redisMap["key1"])
    }

    @Test
    fun testRemove() {
        redisMap.put("key1", 1)
        assertEquals(1, redisMap.remove("key1"))
    }

    @Test
    fun testContainsKey() {
        redisMap.put("key1", 1)
        assertTrue(redisMap.containsKey("key1"))
    }

    @Test
    fun testContainsValue() {
        redisMap.put("key1", 1)
        redisMap.put("key2", 2)
        assertTrue(redisMap.containsValue(1))
        assertFalse(redisMap.containsValue(3))
    }

    @Test
    fun testSize() {
        redisMap.put("key1", 1)
        redisMap.put("key2", 2)
        assertEquals(2, redisMap.size)
    }

    @Test
    fun testIsEmpty() {
        assertTrue(redisMap.isEmpty())
        redisMap.put("key1", 1)
        assertFalse(redisMap.isEmpty())
    }

    @Test
    fun testClear() {
        redisMap.put("key1", 1)
        redisMap.clear()
        assertTrue(redisMap.isEmpty())
    }

    @Test
    fun testPutAll() {
        val data = mapOf("key1" to 1, "key2" to 2)
        redisMap.putAll(data)
        assertEquals(1, redisMap["key1"])
        assertEquals(2, redisMap["key2"])
    }
}
