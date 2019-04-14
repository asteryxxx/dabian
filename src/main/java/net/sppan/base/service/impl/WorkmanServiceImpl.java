package net.sppan.base.service.impl;

import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.*;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.IWorkmanService;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class WorkmanServiceImpl extends BaseServiceImpl<TbWorkman, Integer> implements IWorkmanService {



	@Autowired
	IWorkmanDao workmanDao;
	@Override
	public IBaseDao<TbWorkman, Integer> getBaseDao() {
		return workmanDao;
	}

	@Transactional
	public TbWorkman saveOrUpdate(TbWorkman tbWorkman, int deptId,int postId){
		//判断id是否为空是的话就是添加
		if(tbWorkman.getWid()==null){
			SetWorkmanId(tbWorkman, deptId, postId);

			tbWorkman.setCreateTime(new Date());
            workmanDao.save(tbWorkman);
            TbDept dept = deptDao.findOne(deptId);
            dept.getWorkmanes().add(tbWorkman);
            TbPost post = postDao.findOne(postId);
            post.getWorkmanes().add(tbWorkman);
            deptDao.save(dept);postDao.save(post);
		}else {
			TbWorkman oldword = workmanDao.findOne(tbWorkman.getWid());

            //在更改id前删除外键约束
			SetWorkmanId(oldword, deptId, postId);
			oldword.setDescription(tbWorkman.getDescription());
			oldword.setSort(tbWorkman.getSort());
			oldword.setTelephone(tbWorkman.getTelephone());
		//先移出外键，在设Id,再维护外键
            TbWorkman aa = update(oldword);
            addwaijian(deptId,postId,aa);
            return aa;

		}
		return null;
	}

    public void addwaijian(int deptId, int postId, TbWorkman newwork) {
        TbPost post = postDao.findOne(postId);
        TbDept dept = deptDao.findOne(deptId);
        dept.getWorkmanes().add(newwork);
        post.getWorkmanes().add(newwork);
        deptDao.save(dept);
        postDao.save(post);
    }

	/**
	 *
	 * @param idd  wid 主键
	 * @param tempdeptId  部门id
	 */
	public void delete(int idd, int tempdeptId) {
		TbWorkman man = workmanDao.findOne(idd);
		Integer postid = postManMapper.findPostManbyManId(idd);
		TbPost post = postDao.findOne(postid);
		post.getWorkmanes().remove(man);
		TbDept dept = deptDao.findOne(tempdeptId);
		dept.getWorkmanes().remove(man);
		deptDao.save(dept);postDao.save(post);
		workmanDao.delete(man);
	}

	@Autowired
	PostManMapper postManMapper;

	@Autowired
	IPostDao postDao;

	@Autowired
	IDeptDao deptDao;

	@Transactional
	public void deletewaijian(TbWorkman tbWorkman,int olddeptId, int oldpostId) {
		TbPost post = postDao.findOne(oldpostId);
		TbDept dept = deptDao.findOne(olddeptId);
		Set<TbWorkman> bl1 = dept.getWorkmanes();
		for (TbWorkman a:bl1) {
			Integer awid = a.getWid();
			Integer bwid = tbWorkman.getWid();
			if(awid.equals(bwid)){
				bl1.remove(a);
			}
		}

		Set<TbWorkman> bl2 =post.getWorkmanes();
		for (TbWorkman a:bl2) {
			Integer awid = a.getWid();
			Integer bwid = tbWorkman.getWid();
			if(awid.equals(bwid)){
				bl2.remove(a);
			}
		}
		deptDao.save(dept);postDao.save(post);
	}

	private void SetWorkmanId(TbWorkman tbWorkman, int deptId, int postId) {
		//取出部门和岗位当前缀+00后面是序号
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(deptId);
		strbuf.append(postId);
		int qzid = Integer.parseInt(strbuf.toString());
		Integer intt = workmanDao.findByNameLike(qzid);
		//根据前缀判断表中有没值，没有设置 部门+岗位+00+1 有就在原有的基础上加1
		if(intt==null){
		String idwei="001";
		String newid =  deptId+""+ postId + idwei;
		int manid = Integer.parseInt(newid.toString());
		tbWorkman.setId(manid);
				}else{
					tbWorkman.setId(intt+1);
		}
	}
}
