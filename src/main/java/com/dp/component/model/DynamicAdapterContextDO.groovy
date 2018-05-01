package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 *
 * @description: ${description}
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:27
 * */
class DynamicAdapterContextDO {

    def appId

    /**
     * 自定义的基础默认公共的属性配置
     */
    def DynamicAdapterDefaultDO dynamicAdapterDefaultDO;

    def DynamicAdapterDO dynamicAdapterDO;

    DynamicAdapterContextDO(String appId) {
        this.appId = appId
    }
}