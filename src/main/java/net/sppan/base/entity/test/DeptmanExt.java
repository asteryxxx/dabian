package net.sppan.base.entity.test;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class DeptmanExt {
    Integer id;//部门id
    String deptman;
    String manname;
    Integer manId;
    Integer zt;
    Integer wid;//人员的主键
}
