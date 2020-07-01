package LocalCache;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存工具类
 * 类 {@code CacheUtils} cache的操作.
 *
 * <p> 主要包括 获取缓存、增加缓存、删除缓存、处理缓存过期
 *
 * @author luolizhuo
 * @since 2020/7/1
 */
public class CacheUtils<K, V> {

    private ConcurrentHashMap<Object, Cache> concurrentHashMap;

    final int size;

    public CacheUtils(int capacity) {
        this.size = capacity;
        this.concurrentHashMap = new ConcurrentHashMap<Object, Cache>(capacity);
        new Thread(new TimeoutTimerThread()).start();
    }

    /**
     * 获取缓存
     * @param key 通过键获取
     * @return 获取值
     */
    public Object get(K key) {
        checkNotNull(key);
        if (concurrentHashMap.isEmpty()) return null;
        if (!concurrentHashMap.containsKey(key)) return null;
        Cache cache = concurrentHashMap.get(key);
        if (cache == null) return null;
        return cache.getValue();
    }


    /**
     * 简单增加加缓存
     *
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value) {
        checkNotNull(key);
        checkNotNull(value);
        // 当缓存存在时，更新缓存
        if (concurrentHashMap.containsKey(key)){
            Cache cache = concurrentHashMap.get(key);
            cache.setValue(value);
            return;
        }
        Cache cache = new Cache();
        cache.setKey(key);
        cache.setValue(value);
        concurrentHashMap.put(key, cache);
    }

    /**
     * 增加缓存，并设置过期时间
     *
     * @param key 键
     * @param value 值
     * @param invalid 过期时间
     */
    public void put(K key, V value,long invalid) {
        checkNotNull(key);
        checkNotNull(value);
        // 当缓存存在时，更新缓存
        if (concurrentHashMap.containsKey(key)){
            Cache cache = concurrentHashMap.get(key);
            cache.setInvalidTime(invalid);
            cache.setValue(value);
            return;
        }
        Cache cache = new Cache();
        cache.setKey(key);
        cache.setValue(value);
        cache.setInvalidTime(invalid);
        concurrentHashMap.put(key, cache);
    }

    /**
     * 检查是否为空
     *
     * @param reference 泛型参考
     * @param <T> 泛型
     * @return 看是否为空
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * 判断是否达到最大缓存
     *
     * @return 是否是最大缓存
     */
    private boolean isFull() {

        return concurrentHashMap.size() == size;
    }


    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void remove(String key) {

        this.concurrentHashMap.remove(key);
    }

    /**
     * 删除全部缓存
     *
     */
    public void clear() {
        if(this.concurrentHashMap.size() > 0){
            concurrentHashMap.clear();
        }
    }


    /**
     * 处理过期缓存
     */
    class TimeoutTimerThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(60);
                    invalidCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 创建多久后，缓存失效
         *
         * @throws Exception
         */
        private void invalidCache() throws Exception {
            System.out.println("检测缓存中是否存在过期缓存");
            for (Object key : concurrentHashMap.keySet()) {
                Cache cache = concurrentHashMap.get(key);
                long timoutTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()
                        - cache.getWriteTime());
                if (cache.getInvalidTime() > timoutTime) {
                    continue;
                }
                System.out.println(" 清除过期缓存 ： " + key);
                //清除过期缓存
                concurrentHashMap.remove(key);
            }
        }
    }

}