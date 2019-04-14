package net.sppan.base.service.impl;

import java.util.*;

import net.sppan.base.dao.IRoleDao;
import net.sppan.base.dao.IWorkmanDao;
import net.sppan.base.dao.RoleManMapper;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.RolemanExt;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.exception.CmsCode;
import net.sppan.base.exception.ExceptionCast;
import net.sppan.base.service.IResourceService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.support.impl.BaseServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 角色表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements IRoleService {

	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IResourceService resourceService;
	
	@Override
	public IBaseDao<Role, Integer> getBaseDao() {
		return this.roleDao;
	}

	@Override
	public void saveOrUpdate(Role role) {
		if(role.getId() != null){
			Role dbRole = find(role.getId());
			dbRole.setUpdateTime(new Date());
			dbRole.setName(role.getName());
			dbRole.setDescription(role.getDescription());
			dbRole.setUpdateTime(new Date());
			dbRole.setStatus(role.getStatus());
			update(dbRole);
		}else{

			role.setCreateTime(new Date());
			role.setUpdateTime(new Date());
			save(role);
		}
	}

	
	
	@Override
	public void delete(Integer id) {
		Role role = find(id);
		Assert.state(!"administrator".equals(role.getRoleKey()),"超级管理员角色不能删除");
		super.delete(id);
	}

	@Override
	public void grant(Integer id, String[] resourceIds) {
		Role role = find(id);
		Assert.notNull(role, "角色不存在");

		if(role.getId().equals(1)){
			List<Resource> resourList = resourceService.findAll();
			Set<Resource> set= new HashSet<Resource>();
			set.addAll(resourList);
			role.setResources(set);
			update(role);
		}else {
			//断言判断，如果该角色是admin就不能资源分配，因为他有了全部资源
			Resource resource;
			Set<Resource> resources = new HashSet<Resource>();
			if (resourceIds != null) {
				for (int i = 0; i < resourceIds.length; i++) {
					if (StringUtils.isBlank(resourceIds[i]) || "0".equals(resourceIds[i])) {
						//如果用户选择了全部就是顶级节点就，跳过
						continue;
					}
					Integer rid = Integer.parseInt(resourceIds[i]);
					//选择的节点转成id，根据id找到资源，弄一个set<resource>集合添加找到的资源，更新该用户的所有的资源
					resource = resourceService.find(rid);
					resources.add(resource);
				}
			}
			role.setResources(resources);
			update(role);
		}
	}

	@Override
	public List<TbWorkman> findexistByroleId(int roleId) {
		Role role = roleDao.findOne(roleId);
		Set<TbWorkman> mans = role.getManes();
		List<TbWorkman> cunzailist=new ArrayList<>();
		for (TbWorkman workman : mans) {
			cunzailist.add(workman);
		}
		return cunzailist;
	}

	@Autowired
	IWorkmanDao workmanDao;
	public List<TbWorkman> findnoexistByroleId(int roleId,List<TbWorkman> cunzailist) {
		List<TbWorkman> workmanList = workmanDao.findAll();
		for (TbWorkman xixi : cunzailist) {
			if (workmanList.contains(xixi)) {
				workmanList.remove(xixi);
			}

		}
		return workmanList;
	}

	@Override
	public void grantworkman(Integer id, String[] workmanIds) {
		Role role = find(id);
		Assert.notNull(role, "角色不存在");
		TbWorkman tw;
		Set<TbWorkman> twes = new HashSet<TbWorkman>();
			for (int i = 0; i < workmanIds.length-1; i++) {
				Integer wid = Integer.parseInt(workmanIds[i]);
				tw = workmanDao.findOne(wid);
				twes.add(tw);
			}
		role.setManes(twes);
			update(role);
	}

	@Autowired
	RoleManMapper roleManMapper;
	public List<RolemanExt> selectListByroleman(ConditionRoleMan conditionRoleMan) {
		return roleManMapper.selectListByroleman(conditionRoleMan);
	}

	@Override
	public List<RolemanExt> selectExistManlistByroleid(ConditionRoleMan conditionRoleMan) {
		return roleManMapper.selectExistManlistByroleid(conditionRoleMan);
	}

	@Override
	public List<RolemanExt> selectNoExistManlistByroleid(ConditionRoleMan conditionRoleMan) {
		return roleManMapper.selectNoExistManlistByroleid(conditionRoleMan);
	}

}
