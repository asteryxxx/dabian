package net.sppan.base.service.impl;

import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.DeptManMapper;
import net.sppan.base.dao.IDeptDao;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.excel.ExportDept;
import net.sppan.base.entity.test.DeptmanExt;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
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
public class DeptServiceImpl extends BaseServiceImpl<TbDept, Integer> implements IDeptService {

	@Autowired
	private IDeptDao deptDao;


	@Override
	public IBaseDao<TbDept, Integer> getBaseDao() {
		return this.deptDao;
	}


	@Override
	public void saveOrUpdate(TbDept tbDept) {
		if(tbDept.getId() != null){
			TbDept depttemp = find(tbDept.getId());
			depttemp.setUpdateTime(new Date());
			depttemp.setName(tbDept.getName());
			depttemp.setDeptKey(tbDept.getDeptKey());
			depttemp.setParent(tbDept.getParent());
			update(depttemp);
		}else{
			tbDept.setCreateTime(new Date());
			save(tbDept);
		}
	}

	public File acceptFile(MultipartFile mutipartfile)  {
		File ff = null;
		try {
			String fileName=new String(("导入部门表数据 "+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"UTF-8");
			ff = new File("D:\\"+fileName+".xlsx");
			if (!mutipartfile.isEmpty()) {
                FileOutputStream fos = new FileOutputStream(ff);
                InputStream is = mutipartfile.getInputStream();
                byte[] b = new byte[1024];
                int len;
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
            }
            return ff;
		} catch (IOException e) {
			return null;
		}
	}

	public void fzEntityesTBatchDept(List<String> list) {
		try {
			TbDept parentdept = deptDao.findOne(1);
			List<TbDept> addlist=new ArrayList<>();
			for(int i=0;i<list.size();i++) {
                String str = list.get(i);
                String[] tt = str.split(",");
                String deptname = tt[0];
                String deptkey = tt[1];
                String createtime = tt[2];
                TbDept ed = new TbDept();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(createtime);
                //查看数据库是否有了这条数据，有的话跳过不添加
				TbDept temdept = findcfDept(deptname);
				if(temdept!=null){
					System.out.println("重复了baby");
					continue;
				}
                ed.setName(deptname);
                ed.setDeptKey(deptkey);
                ed.setCreateTime(date);
                ed.setParent(parentdept);
                System.out.println(ed.toString());
                addlist.add(ed);
            }
			deptDao.save(addlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	DeptManMapper deptManMapper;
	public List<DeptmanExt> selectListBydeptman(ConditionRoleMan conditionRoleMan) {
		return deptManMapper.selectListBydeptman(conditionRoleMan);
	}

	public TbDept findcfDept(String deptname) {
		TbDept querydept = new TbDept();
		querydept.setName(deptname);
		Example<TbDept> example = Example.of(querydept);
		return deptDao.findOne(example);
	}
}
