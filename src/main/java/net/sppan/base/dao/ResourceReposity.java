package net.sppan.base.dao;

import net.sppan.base.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ResourceReposity extends JpaRepository<Resource,String>,JpaSpecificationExecutor {
    @Query(nativeQuery = true,value = "SELECT * from tb_resource tr LEFT JOIN tb_role_resource trr on tr.id=trr.resource_id  where trr.role_id=?1")
    List<Resource> findByNameContaining(int roleId);
    @Query(nativeQuery = true,value = "SELECT *from tb_resource where `level`BETWEEN :first and :last ORDER BY `level`,sort")
    List<Resource> findByLevel(@Param("first") Integer first, @Param("last") Integer last);
}
