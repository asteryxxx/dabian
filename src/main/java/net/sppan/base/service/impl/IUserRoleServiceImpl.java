package net.sppan.base.service.impl;

import net.sppan.base.dao.IUserRoleDao;
import net.sppan.base.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserRoleServiceImpl implements IUserRoleService {
    @Autowired
    IUserRoleDao iUserRoleDao;

    public List<Integer> getUserRoleList(int userId) {
        return iUserRoleDao.getUserRoleList(userId);
    }
}
