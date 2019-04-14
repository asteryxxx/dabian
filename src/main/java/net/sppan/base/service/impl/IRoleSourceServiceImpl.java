package net.sppan.base.service.impl;

import net.sppan.base.dao.IRoleSourceDao;
import net.sppan.base.dao.IUserRoleDao;
import net.sppan.base.entity.Resource;
import net.sppan.base.service.IRoleResourceService;
import net.sppan.base.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IRoleSourceServiceImpl implements IRoleResourceService {
    @Autowired
    IRoleSourceDao iRoleSourceDao;


    public List<Integer> getRoleResourceList(int roleId) {
        return iRoleSourceDao.getRoleResourceList(roleId);
    }
}
