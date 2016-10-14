package com.gnet.app.hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.druid.multids.TargetDataSource;
import com.gnet.utils.page.PageUtil;

@Service
public class HelloService {
	
	@Autowired
	private HelloMapper helloMapper;
	
	@Transactional(readOnly = true)
	public Hello findById(String id) {
		Hello hello = new Hello();
		hello.setId(id);
		return this.find(hello);
	}
	
	@Transactional(readOnly = true)
	public Hello find(Hello hello) {
		return helloMapper.selectOne(hello);
	}

	@Transactional(readOnly = true)
	public List<Hello> findAll() {
		return helloMapper.selectAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Hello> pagination(Pageable pageable) {
		return PageUtil.pagination(pageable, new PageUtil.Callback<Hello>() {

			@Override
			public List<Hello> getPageContent() {
				return helloMapper.selectHellos();
			}
		});
	}
	
	@ConditionalOnProperty(name = "spring.datasource2.enable")
	@TargetDataSource(name = "dataSource2")
	@Transactional(readOnly = true)
	public Page<Hello> paginationFromDataSource2(Pageable pageable) {
		return PageUtil.pagination(pageable, new PageUtil.Callback<Hello>() {

			@Override
			public List<Hello> getPageContent() {
				return helloMapper.selectAll();
			}
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(Hello hello) {
		return helloMapper.insertSelective(hello) == 1;
	}
	
	public Boolean createNoTransactional(Hello hello) {
		return helloMapper.insertSelective(hello) == 1;
	}
	
	@ConditionalOnProperty(name = "spring.datasource2.enable")
	@TargetDataSource(name = "dataSource2")
	public Boolean createFromDataSource2NoTransactional(Hello hello) {
		return helloMapper.insertSelective(hello) == 1;
	}
	
}
