package net.sppan.base.entity.support;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class AddressExt implements Serializable {
    Point point;
    String address;
    Address address_detail;
}
