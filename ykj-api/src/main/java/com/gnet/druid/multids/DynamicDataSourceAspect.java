package com.gnet.druid.multids;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切换数据源Advice
 * Order(-1): 保证该AOP在@Transactional之前执行
 */
@Component
@Order(-1)
@Aspect
public class DynamicDataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
	
	@Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        logger.debug("Use DataSource : {} > {}", ds.name(), point.getSignature());
        DynamicDataSourceContextHolder.setDataSourceType(ds.name());
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
	
}
