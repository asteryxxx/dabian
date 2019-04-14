package net.sppan.base.service.impl;

import net.sppan.base.dao.*;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.*;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<TbPost, Integer> implements IPostService {

	@Autowired
	private IPostDao postDao;


	@Override
	public IBaseDao<TbPost, Integer> getBaseDao() {
		return this.postDao;
	}

	@Autowired
	IDeptDao deptDao;
	@Override
	public List<TbPostExt> findbycondition(String deptname, String postname) {
		List<TbPostExt> pxlist=new ArrayList<>();
		List<String> sortProperties = new ArrayList<>();
		sortProperties.add("id");
		Sort sort1 = new Sort(Sort.Direction.ASC, sortProperties);
		List<TbDept> depts = deptDao.findAll(sort1);
		List<TbPostExt> exts=new ArrayList<>();
		for (TbDept dept : depts) {
			TbPostExt ext=null;
			Set<TbPost> posted = dept.getTbpostes();
			String tempdept=dept.getName();
			Integer tempdeptId=dept.getId();

			for (TbPost p1 : posted) {
				if(p1!=null){
					ext=new TbPostExt();
					ext.setId(p1.getId());
					ext.setDeptName(tempdept);
					ext.setDeptId(tempdeptId);
					ext.setPostName(p1.getName());
					ext.setStatus(p1.getStatus());
				}else{
					ext.setPostName("");
				}

				if(p1.getParent()!=null){
					ext.setParentName(p1.getParent().getName());
					ext.setParentId(p1.getParent().getId());
				}else{
					ext.setParentName("");
				}
				exts.add(ext);
			}

		}

			if(postname!=null&&postname!=""&&deptname!=null&&deptname!="" ){
			for (TbPostExt ext : exts) {
				int rs = ext.getPostName().indexOf(postname);
				int rs1 = ext.getDeptName().indexOf(deptname);

				if(rs!=-1&&rs1!=-1){
					pxlist.add(ext);
				}
			}

		}else if(deptname!=null&&deptname!=""){
			for (TbPostExt ext : exts) {
				int rs = ext.getDeptName().indexOf(deptname);
				if(rs!=-1){
					pxlist.add(ext);
				}
			}
		}else if(postname!=null&&postname!=""){
			for (TbPostExt ext : exts) {
				int rs = ext.getPostName().indexOf(postname);
				if(rs!=-1){
					pxlist.add(ext);
				}
			}

		}
			if(postname!=null||deptname!=null ){
				return pxlist;
			}else {
				return exts;
			}
	}

	//可以获取到deptId,parentId,status,postName
	public void saveOrUpdate(TbPostExt tbPostExt,int oldid) {
		Integer postid = tbPostExt.getId();//岗位id
		TbPost temppost = postDao.findOne(postid);
		Integer parent = tbPostExt.getParentId();
		TbPost parentpost = postDao.findOne(parent);
		//找到父
		if (temppost == null) {
			return;
		}
		temppost.setName(tbPostExt.getPostName());
		temppost.setParent(parentpost);
		temppost.setUpdateTime(new Date());
		temppost.setStatus(tbPostExt.getStatus());
		Integer deptid = tbPostExt.getDeptId();
		TbDept dept = deptDao.findOne(deptid);
		dept.getTbpostes().add(temppost);//给部门添加修改后的岗位
		System.out.println("添加了新的岗位");
		//找到原来的部门删除岗位
		TbDept olddept = deptDao.findOne(oldid);
		Iterator it = olddept.getTbpostes().iterator();
		while (it.hasNext()) {
			TbPost post = (TbPost) it.next();
			if (post.getId().equals(temppost.getId())) {
				it.remove();
				System.out.println("删除了旧的岗位");
			}
		}
		deptDao.save(olddept);
		deptDao.save(dept);
		postDao.save(temppost);
	}

	@Override
	public void addfrom(TbPost tbPost, int deptId) {
		if(tbPost.getParent()==null){
			tbPost.setParent(null);
		}
		tbPost.setCreateTime(new Date());
		TbDept dept = deptDao.findOne(deptId);
		dept.getTbpostes().add(tbPost);
		postDao.save(tbPost);
		deptDao.save(dept);
	}

	@Override
	public List<PostmanExt> selectListBypostman(ConditionRoleMan conditionRoleMan) {
		return postManMapper.selectListBypostman(conditionRoleMan);
	}

	@Autowired
	PostManMapper postManMapper;
	public List<PostmanExt> selectExistManlistBypostid(ConditionRoleMan conditionRoleMan) {
		return postManMapper.selectExistManlistBypostid(conditionRoleMan);
	}

	@Override
	public List<PostmanExt> selectNoExistManlistBypostid(ConditionRoleMan conditionRoleMan) {
		return postManMapper.selectNoExistManlistBypostid(conditionRoleMan);
	}

	@Autowired
	IWorkmanDao workmanDao;
	public void grantworkman(Integer id, String[] workmanIds) {
		TbPost post = postDao.findOne(id);
		Assert.notNull(post, "岗位不存在");
		TbWorkman tw;
		Set<TbWorkman> twes = new HashSet<TbWorkman>();
		for (int i = 0; i < workmanIds.length-1; i++) {
			Integer wid = Integer.parseInt(workmanIds[i]);
			tw = workmanDao.findOne(wid);
			twes.add(tw);
		}
		post.setWorkmanes(twes);
		update(post);
	}

	@Autowired
	DeptManMapper deptManMapper;
	public TbDept selectDeptByPostId(int postId) {
		return deptManMapper.selectDeptByPostId(postId);
	}

	@Override
	public Integer findPostManbyManId(int id) {
		return postManMapper.findPostManbyManId(id);
	}
}
