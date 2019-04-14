package net.sppan.base.entity.excel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class ExportDept {
    private String name;
    private String deptKey;
    private Integer xuhao;
    private Date createTime;
}
