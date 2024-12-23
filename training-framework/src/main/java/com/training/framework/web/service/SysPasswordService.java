package com.training.framework.web.service;

import com.training.common.constant.CacheConstants;
import com.training.common.core.cache.MyCache;
import com.training.common.core.domain.entity.SysUser;
import com.training.common.enums.CacheType;
import com.training.common.exception.user.UserPasswordNotMatchException;
import com.training.common.exception.user.UserPasswordRetryLimitExceedException;
import com.training.common.utils.SecurityUtils;
import com.training.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录密码方法
 * 
 * @author training
 */
@Component
public class SysPasswordService
{
    @Resource
    private MyCache myCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = myCache.get(CacheType.LOGIN_RETRY, getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            myCache.put(CacheType.LOGIN_RETRY, getCacheKey(username), retryCount);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        myCache.delete(CacheType.LOGIN_RETRY, getCacheKey(loginName));
    }
}
