package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 *
 * @description: ${description}
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:49
 * */
abstract class AbstractBizAdapter implements BizAdapterInterface{

    /**
     * 数据结构
     */
    protected def info = new HashMap<String, DynamicAdapterContextDO>()

}
