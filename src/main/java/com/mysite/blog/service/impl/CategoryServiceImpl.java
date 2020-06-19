package com.mysite.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysite.blog.mapper.CategoryMapper;
import com.mysite.blog.pojo.Category;
import com.mysite.blog.service.CategoryService;
import com.mysite.blog.uitl.PageRequest;
import com.mysite.blog.uitl.PageResult;
import com.mysite.blog.uitl.PageUtils;
import com.mysite.blog.uitl.PatternUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/5 15:58
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int getCategoryTotal() {
        return categoryMapper.getCategoryTotal();
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<Category> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectPage();
        return new PageInfo<Category>(list);
    }
    /**
     * 添加分类操作
     * @param categoryName
     * @return
     */
    @Override
    public String queryByName(String categoryName) {
        Category category = categoryMapper.queryByName(categoryName);
        if (category != null) {
            if (category.getIsDelete()==0){
                return "existed";
            }else if (category.getIsDelete()==1){
                if(categoryMapper.updateIsDeleteTime(category.getCategoryId())>0){
                    return "success";
                }else {
                    return "error";
                }
            }
        }
        if (categoryMapper.insertCategory(categoryName)>0) {
            return "success";
        } else {
            return "error";
        }
    }
    /**
     * 修改
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public String updateCategory(Integer categoryId, String categoryName) {
        // 通过id判断该分类是否存在
        if(categoryMapper.queryById(categoryId) == null){
            System.out.println("该分类不存在！");
            return "error";
        }
        // 判断该分类名是否重复
        Category category = categoryMapper.queryByName(categoryName);
        if (category != null){
            // 判断重复分类删除状态
            if(category.getIsDelete()==0){
                return "existed";
            }
            // 恢复重复分类名,并且更新时间
            if (categoryMapper.updateIsDelete(category.getCategoryId(),categoryMapper.queryById(categoryId).getCreateTime()) > 0){
                // 删除修改项
                Integer[] ids = new Integer[1];
                ids[0] = categoryId;
                if (categoryMapper.deleteById(ids) > 0){
                    return "success";
                }
            }else {
                return "error";
            }
        }
        Category category1 = new Category();
        category1.setCategoryId(categoryId);
        category1.setCategoryName(categoryName);
        if(categoryMapper.updateDynamicById(category1) > 0){
            return "success";
        }else {
            return "error";
        }
    }

    /**
     * 查询操作
     * @param ids
     * @return
     */
    @Override
    public String queryById(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            if (categoryMapper.queryById(ids[i]) == null) {
                return "risk";
            }
        }
        if (categoryMapper.deleteById(ids) > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectPage();
    }
}
