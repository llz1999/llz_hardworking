package test;

import LocalCache.CacheUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * 类 {@code Test}  读写拷贝以及cache测试
 *
 * <p> 主要包括 cache增删以及过期时间测试
 *
 * @author luolizhuo
 * @since 2020/7/1
 */

public class CacheTest {
    public static void main(String[] args) throws Exception {

        CacheUtils localCache = new CacheUtils(5);
        localCache.put("key", "123456789",5);
        System.out.println(localCache.get("key"));
        TimeUnit.SECONDS.sleep(5);
        System.out.println("清除缓存后："+localCache.get("key"));
        File file = new File("cache.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        bufferedWriter.write("====================\r\n====================");
        bufferedWriter.newLine();
        bufferedWriter.flush();

        //测试本地缓存以及过期时间
        CacheUtils localCache2 = new CacheUtils(5);
        for (int i = 0; i < 3; i++) {
            String[] strings = new String[10];
            localCache2.put("0"+i, "张王李"+i,10);
            strings[i]=i+"张王李";
            bufferedWriter.write("");
            bufferedWriter.write(strings[i]);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }

        localCache2.put("03","123",60);

        for (int i = 0; i < 4; i++) {
            System.out.println(localCache2.get("0"+i));
        }
        //创建BufferedReader读取文件内容
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line=br.readLine())!=null) {
            System.out.println(line);
        }
        br.close();


    }
}