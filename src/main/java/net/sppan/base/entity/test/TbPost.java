package net.sppan.base.entity.test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import net.sppan.base.entity.support.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ToString
@Data
@Entity
@Table(name = "tb_post")
public class TbPost extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  private Integer status;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private TbPost parent;

  @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "tb_post_man", joinColumns = { @JoinColumn(name = "post_id") }, inverseJoinColumns = { @JoinColumn(name = "man_id") })
  private java.util.Set<TbWorkman> workmanes;



}
