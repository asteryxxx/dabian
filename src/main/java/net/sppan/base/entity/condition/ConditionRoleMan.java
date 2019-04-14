package net.sppan.base.entity.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.ConstructorArgs;

@Data
@ToString
public class ConditionRoleMan {
    String roleMan;
    String manName;
    public ConditionRoleMan(String roleMan,
            String manName){
        this.roleMan=roleMan;this.manName=manName;
    }


    public ConditionRoleMan(){

    }
    String postMan;
    String deptMan;

    Integer postId;
    Integer roleId;
}
