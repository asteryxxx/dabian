package net.sppan.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sppan.base.dao.IResourceDao;
import net.sppan.base.dao.ResourceMapper;
import net.sppan.base.dao.ResourceReposity;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.ResourceExt;
import net.sppan.base.entity.Role;
import net.sppan.base.service.IResourceService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import net.sppan.base.vo.ZtreeView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Integer>
		implements IResourceService {


	@Autowired
	private IResourceDao resourceDao;

	@Autowired
	private IRoleService roleService;

	@Override
	public IBaseDao<Resource, Integer> getBaseDao() {
		return this.resourceDao;
	}

	@Override
	@Cacheable(value = "resourceCache", key = "'tree' + #roleId")
	public List<ZtreeView> tree(int roleId) {
		List<ZtreeView> resulTreeNodes = new ArrayList<ZtreeView>();
		//有id,pid,name, boolean(open),boolean checked = false 这几个属性
		Role role = roleService.find(roleId);
		//找到角色
		Set<Resource> roleResources = role.getResources();
		//找到角色的所有的资源集合
		resulTreeNodes.add(new ZtreeView(0L, null, "全部", true));
		ZtreeView node;
		Sort sort = new Sort(Direction.ASC, "parent", "id", "sort");
		//根据三个字段排序，找到排序后的资源集合
		List<Resource> all = resourceDao.findAll(sort);
		for (Resource resource : all) {
			node = new ZtreeView();
			node.setId(Long.valueOf(resource.getId()));
			if (resource.getParent() == null) {
				node.setpId(0L);
			} else {
				node.setpId(Long.valueOf(resource.getParent().getId()));
			}
			node.setName(resource.getName());
			if (roleResources != null && roleResources.contains(resource)) {
				node.setChecked(true);
			}
			resulTreeNodes.add(node);
		}
		return resulTreeNodes;
	}

	@Override
	@CacheEvict(value = "resourceCache")
	public void saveOrUpdate(Resource resource) {
		if(resource.getId() != null){
			Resource dbResource = find(resource.getId());
			dbResource.setUpdateTime(new Date());
			dbResource.setName(resource.getName());
			dbResource.setSourceKey(resource.getSourceKey());
			dbResource.setType(resource.getType());
			dbResource.setSourceUrl(resource.getSourceUrl());
			dbResource.setLevel(resource.getLevel());
			dbResource.setSort(resource.getSort());
			dbResource.setIsHide(resource.getIsHide());
			dbResource.setIcon(resource.getIcon());
			dbResource.setDescription(resource.getDescription());
			dbResource.setUpdateTime(new Date());
			dbResource.setParent(resource.getParent());
			update(dbResource);
		}else{
			resource.setCreateTime(new Date());
			resource.setUpdateTime(new Date());
			save(resource);
		}
	}

	@Autowired
	ResourceMapper resourceMapper;
	public List<ResourceExt> selectList(int roleId) {
		return resourceMapper.selectList(roleId);
	}

	@Autowired
	ResourceReposity resourceReposity;
	public List<Resource> findByLevel(Integer first, Integer last) {
		return resourceReposity.findByLevel(first,last);
	}

	@Override
	@CacheEvict(value = "resourceCache")
	public void delete(Integer id) {
		resourceDao.deleteGrant(id);
		super.delete(id);
	}
	
}
