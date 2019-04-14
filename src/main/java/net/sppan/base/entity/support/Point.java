package net.sppan.base.entity.support;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class Point implements Serializable {
    String x;
    String y;
}
