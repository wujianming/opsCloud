package com.baiyi.opscloud.xterm.handler;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.bae64.CacheKeyUtils;
import com.baiyi.opscloud.xterm.config.XTermConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/25 3:08 下午
 * @Version 1.0
 */
@Component
public class AuditLogHandler {

    private static RedisUtil redisUtil;

    private static XTermConfig xtermConfig;

    @Autowired
    private void setRedisUtil(RedisUtil redisUtil) {
        AuditLogHandler.redisUtil = redisUtil;
    }

    @Autowired
    private void setXTermConfig(XTermConfig xtermConfig) {
        AuditLogHandler.xtermConfig = xtermConfig;
    }

    public static void writeAuditLog(String sessionId, String instanceId) {
        String cacheKey = CacheKeyUtils.getTermAuditLogKey(sessionId, instanceId);
        try {
            if (redisUtil.hasKey(cacheKey)) {
                // 追加内容
                String log = (String)redisUtil.get(cacheKey);
                IOUtils.appendFile(log, xtermConfig.getAuditLogPath(sessionId, instanceId));
                redisUtil.del(cacheKey); // 清空缓存
            }
        } catch (Exception e) {
        }
    }
}
