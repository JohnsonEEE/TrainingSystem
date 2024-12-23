package com.training.web.controller.monitor;

import com.training.common.core.cache.MyCache;
import com.training.common.core.domain.AjaxResult;
import com.training.common.enums.CacheType;
import com.training.system.domain.SysCache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 缓存监控
 * 
 * @author training
 */
@RestController
@RequestMapping("/monitor/cache")
public class CacheController
{
    @Resource
    private MyCache myCache;

    private final static List<SysCache> caches = new ArrayList<>();
    {
        for (CacheType cacheType : CacheType.values()) {
            caches.add(new SysCache(cacheType.getCode(), cacheType.getDesc()));
        }
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
//        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
//        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
//        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
//
//        Map<String, Object> result = new HashMap<>(3);
//        result.put("info", info);
//        result.put("dbSize", dbSize);
//
//        List<Map<String, String>> pieList = new ArrayList<>();
//        commandStats.stringPropertyNames().forEach(key -> {
//            Map<String, String> data = new HashMap<>(2);
//            String property = commandStats.getProperty(key);
//            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
//            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
//            pieList.add(data);
//        });
//        result.put("commandStats", pieList);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public AjaxResult cache()
    {
        return AjaxResult.success(caches);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName)
    {
        Map<String, Object> allCache = myCache.getAllCache(CacheType.getByCode(cacheName));
        List<String> keys = null;
        if (allCache != null) {
            keys = new ArrayList<>(allCache.keySet());
            keys.sort(String::compareTo);
        }
        return AjaxResult.success(keys);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        Object cacheValue = myCache.get(CacheType.getByCode(cacheName), cacheKey);
        return AjaxResult.success(new SysCache(cacheName, cacheKey, Objects.toString(cacheValue)));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName)
    {
        myCache.clear(CacheType.getByCode(cacheName));
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheKey/{cacheName}/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        myCache.delete(CacheType.getByCode(cacheName), cacheKey);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll()
    {
        myCache.clearAll();
        return AjaxResult.success();
    }
}
