package com.dp.component;

import java.io.Serializable;

/**
 * @program: Dynamic_Conversion
 * @description: ddddddd
 * @author: yhongl
 * @create: 2018-05-01 13:23
 **/
public class ResponseDTO  implements Serializable {
    private String id;
    private Long max;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }
}
