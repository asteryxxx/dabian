package net.sppan.base.entity.test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import net.sppan.base.entity.support.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@ToString
@Data
@Entity
@Table(name = "tb_applymoney")
public class TbUserApplymoney extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  private String formkey;//报销的单号
  private String description;//报销具体描述
  private String applyname;//申请人
  private String acceptname;//收款人
  private Integer sum;//金额
  private String formname;//费用报销名字
  private Integer costtype;//费用类型
  private Integer status;//1.待审核 2.审核未通过 3.审核通过


}



