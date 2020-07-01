package test;

import LocalCache.LRUUtils;
import org.junit.Test;

public class LRUTest {
    /**
     * 测试LRU策略缓存回收
     *
     * @throws Exception 抛出异常
     */

    @Test
    public void testLRU() throws Exception{
        LRUUtils<String, String> lruCache = new LRUUtils<String, String>(2);
        for (int i = 0; i < 10; i++) {
            lruCache.put("lru0"+i, "张王李"+i);
        }
        lruCache.put("lru010","123");
        for (int i = 0; i < 11; i++) {
            System.out.println(lruCache.get("lru0"+i));
        }

    }
}
