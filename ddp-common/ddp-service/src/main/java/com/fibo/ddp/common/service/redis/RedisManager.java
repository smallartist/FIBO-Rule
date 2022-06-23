package com.fibo.ddp.common.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.*;

@Component
@Slf4j
public class RedisManager {

    // 0-表示永远不过期
    private int expire = 0;

    @Autowired
    private JedisPool jedisPool;

    public RedisManager() {}

    /**
     * @Title: init
     * @Description: 初始化方法，用来初始化
     * @param     设定文件
     * @return void    返回类型
     * @throws
     */
    public void init() {
    }

    /**
     * @Title: get
     * @Description: 根据key来获得一条特定的缓存数据
     * @param @inputParam key 序列化后的key
     * @param @return    设定文件
     * @return byte[]    返回类型
     * @throws
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            jedis.close();
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }


    /**
     * @Title: get
     * @Description: 根据key来获得一条特定的缓存数据
     * @param @inputParam key string类型的key
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public String get(String key) {
        String value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * @Title: set
     * @Description: 向redis数据库中缓存数据，key，value都为byte[]（已经序列化）
     * @param @inputParam key
     * @param @inputParam value
     * @param @return 设定文件
     * @return byte[] 返回类型
     * @throws
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * @Title: set
     * @Description: 向redis数据库中缓存数据，key，value为字符串类型，缓存时间为永不过期
     * @param @inputParam key
     * @param @inputParam value
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * @Title: set
     * @Description: 向redis数据库中缓存数据，key，value都为byte[]（已经序列化）
     * @param @inputParam key
     * @param @inputParam value
     * @param @inputParam expire 0为永不过期，其他时间则会设置对应的过期时间
     * @param @return 设定文件
     * @return byte[] 返回类型
     * @throws
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * @Title: set
     * @Description: 向redis数据库中缓存数据，key，value都为字符串的类型
     * @param @inputParam key
     * @param @inputParam value
     * @param @inputParam expire 0为永不过期，其他时间则会设置对应的过期时间
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String set(String key, String value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * @Title: del
     * @Description: 根据byte数组（已经序列化的key）来删除redis数据库中缓存的数据
     * @param @inputParam key 设定文件
     * @return void 返回类型
     * @throws
     */
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * @Title: del
     * @Description: 根据特定的string类型的key来删除redis数据库中的缓存数据
     * @param @inputParam key 设定文件
     * @return void 返回类型
     * @throws
     */
    public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @Title: flushDB
     * @Description: 清除指定redis数据库中的数据
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * @Title: dbSize
     * @Description: 获得redis缓存数据的大小
     * @param @return 设定文件
     * @return Long 返回类型
     * @throws
     */
    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return dbSize;
    }

    /**
     * @Title: keys
     * @Description: 根据泛型来查询所有符合泛型的缓存数据
     * @param @inputParam pattern
     * @param @return 设定文件
     * @return Set<byte[]> 返回类型
     * @throws
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return keys;
    }

    /**
     * @Title: dels
     * @Description: 根据提供的泛型来删除reids中缓存的数据
     * @param @inputParam pattern 设定文件
     * @return void 返回类型
     * @throws
     */
    public void dels(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
            Iterator<byte[]> ito = keys.iterator();
            while (ito.hasNext()) {
                jedis.del(ito.next());
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }



    // ------------------------------------ 缓存改造 -----------------------------------

    /**
     * hash类型 添加键值对
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = null;
        try {
            result = jedis.hset(key, field, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * hash类型 获取所有键值对
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Map<String, String> value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.hgetAll(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    public ScanResult<String> scan(String cursor, ScanParams params) {
        ScanResult<String> value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.scan(cursor, params);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    public List<String> scan(String key) {
        List<String> result = null;
        // 游标初始值为0
        String cursor = ScanParams.SCAN_POINTER_START;
        ScanParams scanParams = new ScanParams();
        scanParams.match(key);// pattern
        scanParams.count(1000);
        while (true) {
            //使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
            ScanResult<String> scanResult = scan(cursor, scanParams);
            cursor = scanResult.getStringCursor();// 返回0 说明遍历完成
            List<String> list = scanResult.getResult();
            if (result == null) {
                result = new ArrayList<>();
            }
            result.addAll(list);
            if ("0".equals(cursor)) {
                break;
            }
        }
        return result;
    }

    /**
     * hash类型 模糊查询
     *
     * @param keyPattern
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> scanHash(String keyPattern, Class<T> clazz) {
        List<T> result = null;
        List<String> keys = scan(keyPattern);
        for (String key : keys) {
            Map<String, String> map = hgetAll(key);
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(JSONObject.parseObject(JSONObject.toJSONString(map), clazz));
        }
        return result;
    }

    /**
     * list类型 从尾部添加元素
     *
     * @param key
     * @param strings
     * @return
     */
    public Long rpush(String key, String... strings) {
        Jedis jedis = jedisPool.getResource();
        Long result = null;
        try {
            result = jedis.rpush(key, strings);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * list类型 获取链表中从start到end的元素的值
     * （start、end可为负数，若为-1则表示链表尾部的元素）
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = null;
        try {
            result = jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * set类型 添加成员
     *
     * @param key
     * @param members
     * @return
     */
    public Long sadd(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        Long result = null;
        try {
            result = jedis.sadd(key, members);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * set类型 删除成员
     *
     * @param key
     * @param members
     * @return
     */
    public Long srem(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        Long result = null;
        try {
            result = jedis.srem(key, members);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * set类型 获取所有的成员
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> result = null;
        try {
            result = jedis.smembers(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 根据主键查询对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getByPrimaryKey(String key, Class<T> clazz) {
        long start = System.currentTimeMillis();
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        T result = null;
        Map<String, String> map = hgetAll(key);
        if (map != null) {
            result = JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
        }
        long end = System.currentTimeMillis();
        log.debug("============= getByPrimaryKey ======== 耗时：" + (end -start));
        return result;
    }

    /**
     * 根据外键查询对应的集合
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getByForeignKey(String key, Class<T> clazz) {
        long start = System.currentTimeMillis();
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        List<T> result = null;
        Set<String> members = smembers(key);
        if (members != null) {
            result = hgetAllBatchByPrimaryKeys(new ArrayList<>(members), clazz);
        }
        long end = System.currentTimeMillis();
        log.debug("============= getByForeignKey ======== 耗时：" + (end -start));
        return result;
    }

    /**
     * pipeline批量获取hash数据
     *
     * @param keys 主键列表
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> hgetAllBatchByPrimaryKeys(List<String> keys, Class<T> clazz) {
        long start = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        List<T> result = null;
        try {
            Pipeline pipelined = jedis.pipelined();
            for (String key : keys) {
                pipelined.hgetAll(key);
            }
            // 阻塞拿到所有返回信息
            List<Object> objectList = pipelined.syncAndReturnAll();

            for (Object object : objectList) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(JSONObject.parseObject(JSONObject.toJSONString(object), clazz));
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        long end = System.currentTimeMillis();
        log.debug("============= hgetAllBatchByPrimaryKeys ======== 耗时：" + (end -start));
        return result;
    }

    /**
     * pipeline批量获取hash数据
     * @param keys 外键列表
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> hgetAllBatchByForeignKeys(List<String> keys, Class<T> clazz) {
        long start = System.currentTimeMillis();
        List<T> result = null;
        for (String key : keys) {
            List<T> keyResult = getByForeignKey(key, clazz);
            if(keyResult == null){
                continue;
            }
            if (result == null) {
                result = new ArrayList<>();
            }
            result.addAll(keyResult);
        }
        long end = System.currentTimeMillis();
        log.debug("============= hgetAllBatchByForeignKeys ======== 耗时：" + (end -start));
        return result;
    }

    /**
     * pipeline批量获取string数据
     *
     * @param keys 主键列表
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getAllBatchByPrimaryKeys(List<String> keys, Class<T> clazz) {
        long start = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        List<T> result = null;
        try {
            Pipeline pipelined = jedis.pipelined();
            for (String key : keys) {
                pipelined.get(key);
            }
            // 阻塞拿到所有返回信息
            List<Object> objectList = pipelined.syncAndReturnAll();

            for (Object object : objectList) {
                if (object != null) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(JSONObject.parseObject(object.toString(), clazz));
                }
            }
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        long end = System.currentTimeMillis();
        log.debug("============= getAllBatchByPrimaryKeys ======== 耗时：" + (end - start));
        return result;
    }

    /**
     * pipeline批量插入string数据
     *
     * @param
     * @param
     * @param
     * @return
     */
    public void setAllBatchByPrimaryKeys(Map<String, String> data, int seconds) {
        long start = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        try {
            Pipeline pipelined = jedis.pipelined();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                pipelined.set(entry.getKey(), entry.getValue());
                pipelined.expire(entry.getKey(), seconds);
            }
            // 阻塞拿到所有返回信息
            List<Object> objectList = pipelined.syncAndReturnAll();
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        long end = System.currentTimeMillis();
        log.debug("============= setAllBatchByPrimaryKeys ======== 耗时：" + (end - start));
        return;
    }

    /**
     * @param @inputParam dictKey string类型的key
     * @param @return     设定文件
     * @return String    返回类型
     * @throws
     * @Title: Hget
     * @Description: 根据key来获得一条特定的缓存数据
     */
    public String hsetnx(String key,String field,String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * 获取key的剩余过期时间，单位秒
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Long value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.ttl(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return value;
    }

    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } catch (Exception e) {
            log.error("redis订阅异常:channel:{}", channel, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            log.error("redis发送消息异常", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
