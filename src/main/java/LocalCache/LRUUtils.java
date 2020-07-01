package LocalCache;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存LRU缓存回收工具类
 * 实现思路：
 * <p>插入新的数据时，如果数据项在链表中存在，则把该节点移到链表头部，
 * 如果不存在，则新建一个节点，放到链表头部，若缓存满了，则把链表最后一个节点删除即可。
 * 在访问数据的时候，如果数据项在链表中存在，则把该节点移到链表头部，否则返回-1。
 * 在链表尾部的节点就是最近最久未访问的数据项。
 * 类 {@code LRUUtils} LRU缓存回收策略.
 *
 * <p> 主要包括 获取缓存、增加缓存、缓存回收
 *
 * @author luolizhuo
 * @since 2020/7/1
 */
public class LRUUtils<K, V> {

    private class Node{
        Node prev;
        Node next;
        Object key;
        Object value;

        public Node(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }


    private int capacity;

    private ConcurrentHashMap<Object, Node> concurrentHashMap;

    //如果key在cache中，则返回对应的value值，否则返回-1
    private Node head = new Node(-1, -1);
    private Node tail = new Node(-1, -1);

    public LRUUtils(int capacity) {
        // write your code here
        this.capacity = capacity;
        this.concurrentHashMap = new ConcurrentHashMap<Object, Node>(capacity);
        tail.prev = head;
        head.next = tail;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 通过键获取的值
     */
    public Object get(K key) {
        checkNotNull(key);
        if (concurrentHashMap.isEmpty()) return null;
        if (!concurrentHashMap.containsKey(key)) return null;
        Node current = concurrentHashMap.get(key);
        // 将当前链表移出
        current.prev.next = current.next;
        current.next.prev = current.prev;

        move_to_tail(current);

        return current.value;
    }

    /**
     * 添加缓存
     *
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value) {
        checkNotNull(key);
        checkNotNull(value);
        // 当缓存存在时，更新缓存
        if (concurrentHashMap.containsKey(key)){
            Node current = concurrentHashMap.get(key);
            // 将当前链表移出
            current.prev.next = current.next;
            current.next.prev = current.prev;

            move_to_tail(current);
            return;
        }
        // 已经达到最大缓存
        if (isFull()) {
            concurrentHashMap.remove(head.next.key);
            head.next.next.prev = head;
            head.next = head.next.next;
        }
        Node node = new Node(key,value);
        concurrentHashMap.put(key,node);
        move_to_tail(node);
    }

    /**
     * 检测字段是否合法
     *
     * @param reference 泛型参考
     * @param <T> 泛型
     * @return 判断是否合法
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
     * @return
     */
    private boolean isFull() {
        return concurrentHashMap.size() == capacity;
    }

    private void move_to_tail(Node current) {
        // 将当前链表添加到尾部
        tail.prev.next = current;
        current.prev = tail.prev;
        tail.prev = current;
        current.next = tail;
    }

}
