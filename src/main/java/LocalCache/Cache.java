package LocalCache;


/**
 * 类 {@code Cache} cache的pojo类.
 *
 * <p> 主要包括 缓存值、创建时间、过期时间
 *
 * @author luolizhuo
 * @since 2020/7/1
 */
public class Cache{

    // 键
    private Object key;
    // 缓存值
    private Object value;
    // 创建时间
    private long writeTime;
    // 过期时间
    private long invalidTime;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public long getWriteTime() {
        return writeTime;
    }


    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(long invalidTime) {
        this.invalidTime = invalidTime;
    }



}