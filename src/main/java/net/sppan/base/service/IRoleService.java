package net.sppan.base.service;

import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.RolemanExt;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.support.IBaseService;

import java.util.List;

/**
 * <p>
 * 角色服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IRoleService extends IBaseService<Role,Integer> {

	/**
	 * 添加或者修改角色
	 * @param role
	 */
	void saveOrUpdate(Role role);

	/**
	 * 给角色分配资源
	 * @param id 角色ID
	 * @param resourceIds 资源ids
	 */
	void grant(Integer id, String[] resourceIds);

	/**
	 * 根据角色找到属于他的人员
	 * @param roleId
	 * @return
	 */
	List<TbWorkman> findexistByroleId(int roleId);
	/**
	 * 根据角色找到不属于他的人员
	 * @param roleId
	 * @return
	 */
	List<TbWorkman> findnoexistByroleId(int roleId,List<TbWorkman> cunzailist);
	/**
	 * 给角色分配人员
	 * @param id 角色ID
	 * @param workmanIds 人员ids
	 */
	void grantworkman(Integer id, String[] workmanIds);

	/**
	 * 根据条件查询角色下的人员
	 * @param conditionRoleMan
	 * @return
	 */
	public List<RolemanExt> selectListByroleman(ConditionRoleMan conditionRoleMan);

	public List<RolemanExt> selectExistManlistByroleid(ConditionRoleMan conditionRoleMan);

	public List<RolemanExt> selectNoExistManlistByroleid(ConditionRoleMan conditionRoleMan);

}
