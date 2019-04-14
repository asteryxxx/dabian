package net.sppan.base.entity.pachong;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@ToString
@Data
public class WeatherResult  implements Serializable {
    private String time;//日期
    List<Weather> weatherList;
}
