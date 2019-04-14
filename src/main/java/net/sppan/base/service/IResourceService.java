package net.sppan.base.service;

import java.util.List;

import net.sppan.base.entity.Resource;
import net.sppan.base.entity.ResourceExt;
import net.sppan.base.service.support.IBaseService;
import net.sppan.base.vo.ZtreeView;

/**
 * <p>
 * 资源服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IResourceService extends IBaseService<Resource, Integer> {

	/**
	 * 获取角色的权限树
	 * @param roleId
	 * @return
	 */
	List<ZtreeView> tree(int roleId);

	/**
	 * 修改或者新增资源
	 * @param resource
	 */
	void saveOrUpdate(Resource resource);

	/**
	 *根据角色id获取到权限的菜单
	 * @param roleId
	 * @return
	 */
	public List<ResourceExt> selectList(int roleId);

	/**
	 * 查询前两级，排序后的
	 * @param first
	 * @param last
	 * @return
	 */
	List<Resource> findByLevel( Integer first, Integer last);
}
