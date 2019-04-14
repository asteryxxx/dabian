package net.sppan.base.entity.support;

import lombok.Data;
import lombok.ToString;
import org.junit.Test;

import java.io.Serializable;
@Data
@ToString
public class Address implements Serializable {
    String city;
    int city_code;
    String district;
    String province;
    String street;
    String street_number;
}
