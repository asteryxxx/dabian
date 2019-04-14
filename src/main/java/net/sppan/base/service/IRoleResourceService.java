package net.sppan.base.service;

import net.sppan.base.entity.Resource;

import java.util.List;

public interface IRoleResourceService {
    List<Integer> getRoleResourceList(int roleId);
}
