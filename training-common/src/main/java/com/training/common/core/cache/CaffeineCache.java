/*
 *
 *
 * Copyright ( c ) 2024 TH Supcom Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TH Supcom
 * Corporation ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with TH Supcom Corporation or a TH Supcom
 * authorized reseller (the "License Agreement"). TH Supcom may make changes to the
 * Confidential Information from time to time. Such Confidential Information may
 * contain errors.
 *
 * EXCEPT AS EXPLICITLY SET FORTH IN THE LICENSE AGREEMENT, TH Supcom DISCLAIMS ALL
 * WARRANTIES, COVENANTS, REPRESENTATIONS, INDEMNITIES, AND GUARANTEES WITH
 * RESPECT TO SOFTWARE AND DOCUMENTATION, WHETHER EXPRESS OR IMPLIED, WRITTEN OR
 * ORAL, STATUTORY OR OTHERWISE INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, TITLE, NON-INFRINGEMENT AND FITNESS FOR A
 * PARTICULAR PURPOSE. TH Supcom DOES NOT WARRANT THAT END USER'S USE OF THE
 * SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR SECURE.
 *
 * TH Supcom SHALL NOT BE LIABLE TO END USER, OR ANY OTHER PERSON, CORPORATION OR
 * ENTITY FOR INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY OR CONSEQUENTIAL
 * DAMAGES, OR DAMAGES FOR LOSS OF PROFITS, REVENUE, DATA OR USE, WHETHER IN AN
 * ACTION IN CONTRACT, TORT OR OTHERWISE, EVEN IF TH Supcom HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES. TH Supcom' TOTAL LIABILITY TO END USER SHALL NOT
 * EXCEED THE AMOUNTS PAID FOR THE TH Supcom SOFTWARE BY END USER DURING THE PRIOR
 * TWELVE (12) MONTHS FROM THE DATE IN WHICH THE CLAIM AROSE.  BECAUSE SOME
 * STATES OR JURISDICTIONS DO NOT ALLOW LIMITATION OR EXCLUSION OF CONSEQUENTIAL
 * OR INCIDENTAL DAMAGES, THE ABOVE LIMITATION MAY NOT APPLY TO END USER.
 *
 * Copyright version 2.0
 */
package com.training.common.core.cache;

import com.training.common.enums.CacheType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Caffeine Cache
 *
 * @author yi.yi
 * @date 2024.12.14
 */
@Component
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine")
public class CaffeineCache implements MyCache {
    @Resource
    private CacheManager cacheManager;

    private org.springframework.cache.caffeine.CaffeineCache getCache(CacheType cacheType) {
        return (org.springframework.cache.caffeine.CaffeineCache) cacheManager.getCache(cacheType.getCode());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(CacheType cacheType, String key) {
        Cache.ValueWrapper valueWrapper = getCache(cacheType).get(key);
        return valueWrapper == null ? null : (T) valueWrapper.get();
    }

    @Override
    public void put(CacheType cacheType, String key, Object value) {
        getCache(cacheType).put(key, value);
    }

    @Override
    public Object putIfAbsent(CacheType cacheType, String key, Object value) {
        Cache.ValueWrapper valueWrapper = getCache(cacheType).putIfAbsent(key, value);
        return valueWrapper.get();
    }

    @Override
    public boolean delete(CacheType cacheType, String key) {
        return getCache(cacheType).evictIfPresent(key);
    }

    @Override
    public Map<String, Object> getAllCache() {
        Map<String, Object> allCache = new HashMap<>();
        for (CacheType cacheType : CacheType.values()) {
            allCache.putAll(getAllCache(cacheType));
        }
        return allCache;
    }

    @Override
    public Map<String, Object> getAllCache(CacheType cacheType) {
        ConcurrentMap<@NonNull Object, @NonNull Object> asMap = getCache(cacheType).getNativeCache().asMap();
        Map<String, Object> stringMap = new HashMap<>();
        if (asMap != null) {
            for (Map.Entry<Object, Object> entry : asMap.entrySet()) {
                stringMap.put((String) entry.getKey(), entry.getValue());
            }
        }
        return stringMap;
    }

    @Override
    public void clear(CacheType cacheType) {
        getCache(cacheType).clear();
    }

    @Override
    public void clearAll() {
        for (CacheType value : CacheType.values()) {
            clear(value);
        }
    }
}
