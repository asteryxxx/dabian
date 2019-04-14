package net.sppan.base.entity.cost;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.support.BaseEntity;

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
@ToString
@Table(name = "tb_costtype")
public class CostType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 费用名
	 */
	private String name;





	/**
	 * 逻辑删除状态 0 未删除 1 删除
	 */
	private Integer status;




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





}
