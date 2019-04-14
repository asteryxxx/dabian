package net.sppan.base.entity;



import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@ToString
public class ResourceExt extends  Resource{
    String text;
    List<ResourceExt> nodes;
}
