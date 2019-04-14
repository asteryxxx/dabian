package net.sppan.base.service;

import net.sppan.base.entity.User;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.support.IBaseService;

import java.util.List;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IWorkmanService extends IBaseService<TbWorkman, Integer> {

	List<TbWorkman> findAll();


    TbWorkman saveOrUpdate(TbWorkman tbWorkman, int deptId, int postId );

    void deletewaijian(TbWorkman tbWorkman,int olddeptId, int oldpostId);

    void addwaijian(int deptId, int postId, TbWorkman newwork);

    void delete(int idd, int tempdeptId);
}
