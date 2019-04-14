package net.sppan.base.entity;

import java.util.Date;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import net.sppan.base.entity.support.BaseEntity;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbUserApplymoney;
import net.sppan.base.entity.test.TbWorkman;

/**
 * <p>
 * 用户账户表
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Entity
@Data
@Table(name = "tb_user")
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 账户名
	 */
	private String userName;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 性别 0 女 1 男
	 */
	private Integer sex;

	/**
	 * 出生日期
	 */
	@JSONField(format = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 电话
	 */
	private String telephone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 住址
	 */
	private String address;

	/**
	 * 逻辑删除状态 0 未删除 1 删除
	 */
	private Integer deleteStatus;

	/**
	 * 是否锁定
	 * 
	 * 0 未锁定 1 锁定
	 */
	private Integer locked;

	/**
	 * 用户描述
	 */
	private String description;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private java.util.Set<Role> roles;

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_user_applymoney", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "form_id") })
	private java.util.Set<TbUserApplymoney> applymoneyes;


	/*@OneToOne(cascade=CascadeType.ALL)//People是关系的维护端
	@JoinTable(name = "tb_user_man",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name = "workman_id"))//通过关联表保存一对一的关系
	private TbWorkman workman;*/


}
