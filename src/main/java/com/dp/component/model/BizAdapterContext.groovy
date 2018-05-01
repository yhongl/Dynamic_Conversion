package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 * @description: 单实例存储及获取当前配置
 * @author: yhongl
 * @create: 2018-05-01 13:23
 **/
@Singleton
class BizAdapterContext extends AbstractBizAdapter {


    /**
    * @Description: 结构存储
    * @Param:
    * @return:
    * @Author: yhongl
    * @Date: 2018/5/1
    */
    def void putItem(String key, DynamicAdapterContextDO item, boolean replace = true) {
        if (!key || !item) {
            throw new RuntimeException('put item is null')
        }
        def getOrDefault = info.getOrDefault(key, null)
        if (replace && getOrDefault){
            info.remove(key)
        }
        info.put(key,item)
    }

    /**
    * @Description: 获取指定Key信息
    * @Param:
    * @return:
    * @Author: yhongl
    * @Date: 2018/5/1
    */
    def DynamicAdapterContextDO getItem(String key) {
        if (!key) {
            return null
        }
        return info.getOrDefault(key, null)
    }


}