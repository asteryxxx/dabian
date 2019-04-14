package net.sppan.base.service;

import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.PostmanExt;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbPostExt;
import net.sppan.base.service.support.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 岗位类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IPostService extends IBaseService<TbPost,Integer> {
    List<TbPostExt> findbycondition(String deptname,String postname);

    void saveOrUpdate(TbPostExt tbPostExt,int oldid);

    void addfrom(TbPost tbPost, int deptId);

    public List<PostmanExt> selectListBypostman(ConditionRoleMan conditionRoleMan);

    public List<PostmanExt> selectExistManlistBypostid(ConditionRoleMan conditionRoleMan);
    /**
     * 查询不在该岗位下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<PostmanExt> selectNoExistManlistBypostid(ConditionRoleMan conditionRoleMan);

    void grantworkman(Integer id, String[] workmanIds);

    public TbDept selectDeptByPostId(int postId);

    public Integer findPostManbyManId(int id);
}
