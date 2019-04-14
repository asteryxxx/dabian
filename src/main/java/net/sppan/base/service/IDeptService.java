package net.sppan.base.service;

import net.sppan.base.entity.Resource;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.DeptmanExt;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.support.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 部门服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IDeptService extends IBaseService<TbDept,Integer> {

    /**
     * 修改或者新增资源
     * @param resource
     */
    void saveOrUpdate(TbDept tbDept);

    public File acceptFile(MultipartFile mutipartfile) ;

    void fzEntityesTBatchDept(List<String> list);

    /**
     * 查询部门下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<DeptmanExt> selectListBydeptman(ConditionRoleMan conditionRoleMan);


}
