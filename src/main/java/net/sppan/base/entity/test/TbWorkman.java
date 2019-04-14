package net.sppan.base.entity.test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import net.sppan.base.entity.User;
import net.sppan.base.entity.support.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ToString
@Data
@Entity
@Table(name = "tb_workman")
public class TbWorkman extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer wid;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  private Integer id;//人员标识
  private  String telephone;
  private String name;
  private Integer status;
  private Integer sort;
  private String description;
 /* @OneToOne(cascade=CascadeType.ALL)//People是关系的维护端
  @JoinTable(name = "tb_user_man",
          joinColumns = @JoinColumn(name="workman_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))//通过关联表保存一对一的关系
  private User user;*/


}
