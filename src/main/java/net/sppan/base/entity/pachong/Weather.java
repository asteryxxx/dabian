package net.sppan.base.entity.pachong;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@ToString
@Data
public class Weather implements Serializable{
    private String mornornight;//白天还是晚上
    private String name;//天气状况
    private String wendu;
    private String fengli;//风力
}
