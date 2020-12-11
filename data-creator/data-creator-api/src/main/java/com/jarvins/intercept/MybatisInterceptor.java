package com.jarvins.intercept;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.executor.statement.StatementHandler;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.util.StopWatch;

import java.sql.Statement;


@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Slf4j
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler target = (StatementHandler) invocation.getTarget();
        String sql = target.getBoundSql().getSql();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = invocation.proceed();
        stopWatch.stop();
        long consume = stopWatch.getTotalTimeMillis();
        if (consume > 50) {
            log.warn("execute sql :{}, consume: {}ms, more than 100ms.", sql, consume);
        } else {
            log.info("execute sql :{}, consume: {}ms.", sql, consume);
        }
        return proceed;
    }
}
