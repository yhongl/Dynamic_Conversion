package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 *
 * @description: ${description}
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:47
 * */
interface BizAdapterInterface {


    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: yhongl
    * @Date: 2018/5/1
    */
    void putItem(String key, DynamicAdapterContextDO item, boolean replace);

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: yhongl
    * @Date: 2018/5/1
    */
    DynamicAdapterContextDO getItem(String key);
}