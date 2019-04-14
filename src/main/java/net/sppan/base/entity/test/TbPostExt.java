package net.sppan.base.entity.test;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class TbPostExt  {
    Integer deptId;
    Integer parentId;
    Integer id;
    String deptName;
    String postName;
    String parentName;
    Integer status;
}
