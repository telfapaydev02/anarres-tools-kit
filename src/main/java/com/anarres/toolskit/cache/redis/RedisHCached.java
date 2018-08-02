package com.anarres.toolskit.cache.redis;

import com.anarres.toolskit.cache.HCache;
import com.anarres.toolskit.support.FuncKit;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *	redis 缓存实现。
 * 由于redis本身便支持 HMap (map里面包含map)的方式来存储数据。
 * 因此Redis的Cached实现更
 */
public class RedisHCached implements HCache {
	public RedisHCached() {

	}
	// -1 - never expire
	private int expire = -1;
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public String deleteCached(final byte[] key) {
		redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.del(key);
            return null;
        });
		return null;
	}

	@Override
	public String updateCached(final byte[] key, final byte[] session,final Long expireSec) {
		return (String) redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.set(key, session);
            if(expireSec!=null){
                connection.expire(key, expireSec);
            }else{
                connection.expire(key, expire);
            }
            return new String(key);
        });

	}

	@Override
	public Object getCached(final byte[] key) {
		return redisTemplate.execute((RedisCallback<Object>) connection -> {
            byte[] bs = connection.get(key);
            return FuncKit.unserialize(bs);
        });

	}


	@Override
	public Set getKeys(final byte[] keys) {
		return redisTemplate.execute((RedisCallback<Set>) connection -> {
            Set<byte[]> setByte = connection.keys(keys);
            if (setByte == null || setByte.size() < 1) {
                return null;
            }
            Set set = new HashSet();
            for (byte[] key : setByte) {
                byte[] bs = connection.get(key);
                set.add(FuncKit.unserialize(bs));
            }

            return set;

        });
	}



	@Override
	public Set getHashKeys(final byte[] key) {
		return redisTemplate.execute((RedisCallback<Set>) connection -> {
            Set<byte[]> hKeys = connection.hKeys(key);
            if(hKeys==null||hKeys.size()>1){
                return null;
            }
            Set set=new HashSet();
            for(byte[] bs:hKeys){
                set.add(FuncKit.unserialize(bs));
            }
        return set;
        });

	}

	@Override
	public Boolean  updateHashCached(final byte[] key,final byte[] mapkey, final byte[] value, final Long expireSec) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            Boolean hSet = connection.hSet(key, mapkey, value);

            return hSet;
        });
	}

	@Override
	public Object getHashCached(final byte[] key, final byte[] mapkey) {
		return redisTemplate.execute((RedisCallback<Object>) connection -> {
            byte[] hGet = connection.hGet(key, mapkey);
            return FuncKit.unserialize(hGet);

        });
	}
	
	
	@Override
	public Long deleteHashCached(final byte[] key, final byte[] mapkey) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
            Long hDel = connection.hDel(key, mapkey);
            return hDel;
        });
	}
	
	
	@Override
	public Long getHashSize(final byte[] key) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
            Long len = connection.hLen(key);

            return len;

        });
	}

	
	@Override
	public Long getDBSize() {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
            Long len = connection.dbSize();

            return len;

        });
	}

	@Override
	public void clearDB() {
		 redisTemplate.execute((RedisCallback<Long>) connection -> {
               connection.flushDb();
             return null;

         });
	}
	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	@Override
	public List getHashValues(final byte[] key) {
		return redisTemplate.execute((RedisCallback<List>) connection -> {
             List<byte[]> hVals = connection.hVals(key);

             if(hVals==null||hVals.size()<1){
                 return null;
             }
             List list=new ArrayList();

             for(byte[] bs:hVals){
                 list.add(FuncKit.unserialize(bs));
             }
            return list;

        });
	}




	

}
