package net.sppan.base.entity.test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.support.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ToString
@Data
@Entity
@Table(name = "tb_dept")
public class TbDept  extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  private String name;
  private String deptKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private TbDept parent;

  @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "tb_dept_post", joinColumns = { @JoinColumn(name = "dept_id") }, inverseJoinColumns = { @JoinColumn(name = "post_id") })
  private java.util.Set<TbPost> tbpostes;


  @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "tb_dept_man", joinColumns = { @JoinColumn(name = "dept_id") }, inverseJoinColumns = { @JoinColumn(name = "man_id") })
  private java.util.Set<TbWorkman> workmanes;
}



