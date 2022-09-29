package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * 三级分类树形结构
 * 支持无限层级
 * 当前项目只有三级
 */
@Data
public class CategoryTreeTo {

    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;

}
