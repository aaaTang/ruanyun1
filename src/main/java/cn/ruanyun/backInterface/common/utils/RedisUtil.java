package cn.ruanyun.backInterface.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static RedisUtil that;

    /**
     * init()之前执行
     */
    @PostConstruct
    protected  void Init()
    {
        that = this;
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key){

        if(key!=null && key.length > 0){

            if(key.length == 1){
                that.redisTemplate.delete(key[0]);
            }else{
                that.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 是否存在KEY
     * @param key
     * @return
     */
    public static boolean hasKey(String key){
        return that.redisTemplate.hasKey(key);
    }


    /**
     * 批量删除<br>
     * （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
     * @param pattern
     */
    public static void batchDel(String... pattern){
        for (String kp : pattern) {
            that.redisTemplate.delete(that.redisTemplate.keys(kp + "*"));
        }
    }


    /**
     * 取得缓存（int型）
     * @param key
     * @return
     */
    public static Integer getInt(String key){
        String value = that.redisTemplate.boundValueOps(key).get();
        return Optional.ofNullable(value).map(values -> Integer.parseInt(values)).orElse(null);
    }


    /**
     * 取得缓存（字符串类型）
     * @param key
     * @return
     */
    public static String getStr(String key){
        return that.redisTemplate.boundValueOps(key).get();
    }



    /**
     * 取得缓存（字符串类型）
     * @param key
     * @return
     */
    public static String getStr(String key, boolean retain){

        String value = that.redisTemplate.boundValueOps(key).get();
        if(!retain){
            that.redisTemplate.delete(key);
        }
        return value;
    }



    /**
     * 获取缓存<br>
     * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
     * @param key

     * @return
     */
    public static Object getObj(String key){
        return that.redisTemplate.boundValueOps(key).get();
    }

    /**
     * 获取缓存<br>
     * 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
     * @param key
     * @param retain    是否保留
     * @return
     */
    public static Object getObj(String key, boolean retain){
        Object obj = that.redisTemplate.boundValueOps(key).get();
        if(!retain){
            that.redisTemplate.delete(key);
        }
        return obj;
    }

    /**

     * 获取缓存<br>
     * 注：该方法暂不支持Character数据类型
     * @param key   key
     * @param clazz 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        return (T)that.redisTemplate.boundValueOps(key).get();
    }


    /**
     * 获取缓存json对象<br>
     * @param key   key
     * @param clazz 类型
     * @return
     */
    public static <T> T getJson(String key, Class<T> clazz) {
        return com.alibaba.fastjson.JSONObject.parseObject(that.redisTemplate.boundValueOps(key).get(), clazz);
    }


    public static void set(String key, Object value){
        set(key, value, null);
    }



    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */

    public static void set(String key, Object value, Integer time){

        if(value.getClass().equals(String.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Integer.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Double.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Float.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Short.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Long.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Boolean.class)){
            that.redisTemplate.opsForValue().set(key, value.toString());
        }else{
            that.redisTemplate.opsForValue().set(key, value.toString());
        }
        if(time != null && time > 0){
            that.redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }


    /**
     * 将value对象以JSON格式写入缓存
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setJson(String key, Object value, Integer time){

        if (ToolUtil.isNotEmpty(getObj(key))){
            del(key);
        }
        that.redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
        if(time > 0){
        that.redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }


    /**
     * 更新key对象field的值
     * @param key   缓存key
     * @param field 缓存对象field
     * @param value 缓存对象field值
     */
    public static void setJsonField(String key, String field, String value){
        JSONObject obj = JSON.parseObject(that.redisTemplate.boundValueOps(key).get());
        obj.put(field, value);
        that.redisTemplate.opsForValue().set(key, obj.toJSONString());
    }


    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    public static Long decr(String key, Long by){
        return that.redisTemplate.opsForValue().increment(key, -by);
    }



    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    public static Long incr(String key, Long by){
        return that.redisTemplate.opsForValue().increment(key, by);
    }


    /**

     * 获取double类型值

     * @param key

     * @return

     */

    public static double getDouble(String key) {
        String value = that.redisTemplate.boundValueOps(key).get();
        if(ToolUtil.isNotEmpty(value)){
            return Double.parseDouble(value);
        }
        return 0d;
    }



    /**
     * 设置double类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setDouble(String key, double value, Date time) {
        that.redisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time.getTime() > 0){
            that.redisTemplate.expire(key, time.getTime(), TimeUnit.SECONDS);
        }
    }


    /**
     * 设置double类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setInt(String key, int value, Long time) {
        that.redisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time != null && time > 0){
            that.redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }




    /**
     * 将map写入缓存
     * @param key
     * @param map
     * @param time 失效时间(秒)
     */
    public static <T> void setMap(String key, Map<String, T> map, Date time){
        that.redisTemplate.opsForHash().putAll(key, map);
    }



    /**
     * 将map写入缓存
     * @param time 失效时间(秒)
     */
  @SuppressWarnings("unchecked")
    public static <T> void setMap(String key, T obj, Date time){
        Map<String, String> map = JSONObject.parseObject((byte[]) obj, Map.class);
         that.redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * 向key对应的map中添加缓存对象
     * @param key
     * @param map
     */
    public static <T> void addMap(String key, Map<String, T> map){
        that.redisTemplate.opsForHash().putAll(key, map);
    }

    /**

     * 向key对应的map中添加缓存对象

     * @param key   cache对象key

     * @param field map对应的key

     * @param value     值

     */

    public static void addMap(String key, String field, String value){
        that.redisTemplate.opsForHash().put(key, field, value);
    }

    /**

     * 向key对应的map中添加缓存对象

     * @param key   cache对象key

     * @param field map对应的key

     * @param obj   对象

     */

    public static <T> void addMap(String key, String field, T obj){

        that.redisTemplate.opsForHash().put(key, field, obj);

    }



    /**

     * 获取map缓存

     * @param key

     * @param clazz

     * @return

     */
    public static <T> Map<String, T> mget(String key, Class<T> clazz){
        BoundHashOperations<String, String, T> boundHashOperations = that.redisTemplate.boundHashOps(key);
        return boundHashOperations.entries();

    }


    public static String getMapField(String key, String field){
        return (String)that.redisTemplate.boundHashOps(key).get(field);
    }

    /*

     * 获取map缓存

     * @param key

     * @param clazz

     * @return*/

    /*public static <T> T getMap(String key, Class<T> clazz){

        BoundHashOperations<String, String, String> boundHashOperations = that.redisTemplate.boundHashOps(key);

   Map<String, String> map = boundHashOperations.entries();

        return JsonMapper.parseObject(map, clazz);

    }*/

    /**

     * 获取map缓存中的某个对象

     * @param key

     * @param field

     * @param clazz

     * @return

     */

    @SuppressWarnings("unchecked")

    public static <T> T getMapField(String key, String field, Class<T> clazz){

        return (T)that.redisTemplate.boundHashOps(key).get(field);

    }

    /**

     * 删除map中的某个对象

     * @author lh

     * @date 2016年8月10日

     * @param key   map对应的key

     * @param field map中该对象的key

     */

    public static void delMapField(String key, String... field){

        BoundHashOperations<String, String, ?> boundHashOperations = that.redisTemplate.boundHashOps(key);

        boundHashOperations.delete((Object) field);

    }

    /**

     * 指定缓存的失效时间

     *

     * @author FangJun

     * @date 2016年8月14日

     * @param key 缓存KEY

     * @param time 失效时间(秒)

     */

    public static void expire(String key, Date time) {

        if(time.getTime() > 0){

            that.redisTemplate.expire(key, time.getTime(), TimeUnit.SECONDS);

        }

    }

    /**

     * 添加set

     * @param key

     * @param value

     */

    public static void sadd(String key, String... value) {

        that.redisTemplate.boundSetOps(key).add(value);

    }

    /**

     * 删除set集合中的对象

     * @param key

     * @param value

     */

    public static void srem(String key, String... value) {

        that.redisTemplate.boundSetOps(key).remove((Object) value);

    }

    /**

     * set重命名

     * @param oldkey

     * @param newkey

     */

    public static void srename(String oldkey, String newkey){

        that.redisTemplate.boundSetOps(oldkey).rename(newkey);

    }

/**

 * 短信缓存

 * @author fxl

 * @date 2016年9月11日

 * @param key

 * @param value

 * @param time

 */

  public static void setIntForPhone(String key,Object value,int time) {

      that.redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));

      if (time > 0) {

          that.redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
  }
}
