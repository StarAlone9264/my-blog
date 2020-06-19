package com.mysite.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/5 15:47
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {

    private Integer categoryId;
    private String categoryName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Integer hotRank;
    private Integer isDelete;

}
