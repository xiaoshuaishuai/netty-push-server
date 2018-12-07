package com.personal.cable.model.bo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/4 20:36
 * @description:
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class WsPushBO {
    private String title;
    private String message;
    private String author;
}
