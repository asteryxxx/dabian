package net.sppan.base.entity.test;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class PageRoleManEx {
        long totalElements;
        List<RolemanExt> content;
        long totalPages;
}
