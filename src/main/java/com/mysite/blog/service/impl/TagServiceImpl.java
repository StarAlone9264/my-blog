package com.mysite.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysite.blog.mapper.TagMapper;
import com.mysite.blog.pojo.BlogTagCount;
import com.mysite.blog.pojo.Tag;
import com.mysite.blog.service.TagService;
import com.mysite.blog.uitl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/6 16:03
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public int getTagTotal() {
        return tagMapper.getTagTotal();
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }

    /**
     * 添加标签
     * @param tagName
     * @return
     */
    @Override
    public Result queryByName(String tagName) {
        Tag tag = tagMapper.queryByName(tagName);
        if (tag ==null){
            if(tagMapper.insertTag(tagName) > 0){
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("操作异常");
            }
        }else {
            if (tag.getIsDelete() == 1){
                if(tagMapper.updateIsDelete(tag.getTagId()) > 0){
                    return ResultGenerator.genSuccessResult();
                }
            }
            return ResultGenerator.genFailResult("已存在");
        }
    }

    /**
     * 删除操作
     * @param ids
     * @return
     */
    @Override
    public Result queryById(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            if (tagMapper.queryById(ids[i])==null){
                return ResultGenerator.genFailResult("操作异常");
            }
        }
        if (tagMapper.deleteById(ids) > 0){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("操作异常");
        }
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<Tag> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Tag> list = tagMapper.selectPage();
        return new PageInfo<>(list);
    }

    /**
     * 获取热门标签
     * @return
     */
    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        return tagMapper.getTagCount();
    }
}
