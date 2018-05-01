package com.dp.component.dynamic.context

import groovy.transform.ToString

/**
 * @program: Dynamic_Conversion
 *
 * @description: ${description}
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:35
 * */
@Singleton
@ToString(includeNames = true, includeFields = true)
class DefaultCommonDef {

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: yhongl
    * @Date: 2018/5/1
    */
    def static funCustimze(item, elementValue, funName) {
        if (!funName) {
            return elementValue
        }
        if (elementValue instanceof List || elementValue instanceof HashMap) {
            return elementValue
        }

        def appKey = item.attribute("appKey")
        def dateFmt = item.attribute("dateFmt")
//        CustomizeFunCollection.instance.onAction(funName, elementValue, appKey, dateFmt);
    }




}
