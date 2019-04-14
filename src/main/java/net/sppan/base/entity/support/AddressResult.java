package net.sppan.base.entity.support;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class AddressResult implements Serializable{
    String address;
    AddressExt content;
    int status;
}
