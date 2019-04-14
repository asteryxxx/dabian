package net.sppan.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import net.sppan.base.entity.support.BaseEntity;
import net.sppan.base.entity.test.TbUserApplymoney;

import javax.persistence.*;
import java.util.Date;

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
@Table(name = "tb_log")
public class Logs extends BaseEntity {

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
	 * 用户浏览器
	 */
	private String userAgent;

	/**
	 * exception
	 */
	private String exception;

	/**
	 * 用户登录的id
	 */
	private Integer userId;


	/**
	 * 操作标题
	 */
	private String title;

	/**
	 * 请求url
	 */
	private String requestUri;

	/**
	 * 请求参数
	 */
	private String params;



	/**
	 * 请求方法
	 */
	private String method;



	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastTime;


}
