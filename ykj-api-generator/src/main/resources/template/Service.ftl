package ${packageDir}.${toCamelName(resourceName)};

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class ${toCamelName(resourceName)?cap_first}Service {
	
	@Autowired
	private ${toCamelName(resourceName)?cap_first}Mapper ${toCamelName(resourceName)}Mapper;
	
	public ${toCamelName(resourceName)?cap_first} findById(String id) {
		return ${toCamelName(resourceName)}Mapper.selectByPrimaryKey(id);
	}
	
	public ${toCamelName(resourceName)?cap_first} find(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		return ${toCamelName(resourceName)}Mapper.selectOne(${toCamelName(resourceName)});
	}
	
	public List<${toCamelName(resourceName)?cap_first}> findAll(List<String> orderList) {
		return findAll(<#if (searchMap.entrySet()?size > 0) ><#list searchMap.entrySet() as item>null, </#list></#if>orderList);
	}
	
	public List<${toCamelName(resourceName)?cap_first}> findAll(<#if (searchMap.entrySet()?size > 0) ><#list searchMap.entrySet() as item>${item.value} ${toCamelName(item.key)}, </#list></#if>List<String> orderList) {
		return ${toCamelName(resourceName)}Mapper.select${toCamelName(resourceNameComplex)?cap_first}AllList(orderList, <#if (searchMap.entrySet()?size > 0) ><#list searchMap.entrySet() as item>${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list></#if><#if "${isSoftDel}" == "true" >, ${toCamelName(resourceName)?cap_first}.DEL_FALSE</#if>);
	}
	
	<#if (searchMap.entrySet()?size > 0) >
	public Page<${toCamelName(resourceName)?cap_first}> pagination(Pageable pageable, List<String> orderList) {
		return pagination(pageable<#list searchMap.entrySet() as item>, null</#list>, orderList);
	}
	
	public Page<${toCamelName(resourceName)?cap_first}> pagination(Pageable pageable<#list searchMap.entrySet() as item>, ${item.value} ${toCamelName(item.key)}</#list>, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<${toCamelName(resourceName)?cap_first}>() {
			
			@Override
			public List<${toCamelName(resourceName)?cap_first}> getPageContent() {
				return ${toCamelName(resourceName)}Mapper.select${toCamelName(resourceNameComplex)?cap_first}All(<#list searchMap.entrySet() as item>${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list><#if "${isSoftDel}" == "true" >, ${toCamelName(resourceName)?cap_first}.DEL_FALSE</#if>);
			}
			
		});
	}
	<#else>
	public Page<${toCamelName(resourceName)?cap_first}> pagination(Pageable pageable, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<${toCamelName(resourceName)?cap_first}>() {
			
			@Override
			public List<${toCamelName(resourceName)?cap_first}> getPageContent() {
				return ${toCamelName(resourceName)}Mapper.selectAll();
			}
			
		});
	}
	</#if>
	
	@Transactional(readOnly = false)
	public Boolean create(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		return ${toCamelName(resourceName)}Mapper.insertSelective(${toCamelName(resourceName)}) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		return ${toCamelName(resourceName)}Mapper.updateByPrimaryKeySelective(${toCamelName(resourceName)}) == 1;
	}
	
	
	<#if "${isSoftDel}" == "true" >
	@Transactional(readOnly = false)
	public Boolean delete(String id, String userId, Date date) {
		return ${toCamelName(resourceName)}Mapper.deleteById(id, userId, date) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean deleteAll(String[] ids, String userId, Date date) {
		if(ids == null) {
			return true;
		}
		
		return ${toCamelName(resourceName)}Mapper.deleteByIds(ids, userId, date) == ids.length;
	}
	<#else>
	@Transactional(readOnly = false)
	public Boolean delete(String id) {
		return ${toCamelName(resourceName)}Mapper.deleteByPrimaryKey(id) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean deleteAll(String[] ids) {
		if(ids == null) {
			return true;
		}
		
		return ${toCamelName(resourceName)}Mapper.deleteByIds(ids) == ids.length;
	}
	</#if>
	
}