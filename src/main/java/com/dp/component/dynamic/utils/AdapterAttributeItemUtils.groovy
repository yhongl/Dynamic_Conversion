package com.dp.component.dynamic.utils


/**
 *
 * 专门用于扩展xml逻辑使用
 * @Author:yhongl
 * @Date :2017/9/20
 * @Time :9:44 
 */
class AdapterAttributeItemUtils {

/**
 * 提取指定节点属性信息
 */
    def static initLoopSourceNode = { nd, String packageNames ->
        if (!packageNames) {
            return nd
        }
        def packageArrayList = packageNames.indexOf(".") ? packageNames.split("\\.") : [packageNames];
        try {
            packageArrayList.each { nodeItem ->
                nd = nd."$nodeItem"
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        nd
    }

/**
 * 初始化返回实例对象
 */
    def static initLoopClass = { dto, String clsNames ->
        java.lang.Object objDto = (java.lang.Object) dto;
        clsNames.split("\\.").each { item ->
            java.lang.Object itemCls = objDto["$item"];
            objDto = itemCls;
        }
        objDto
    }

    def static convertOfDetails = { ruleNode, sourceNode, dto, String classPath, type ->

        ruleNode.children().each { item ->
            def mpName = item.attribute("mp");//原始xml中节点名
            def dtoName = item.attribute("stand");//获取Dto对应名
            def xmlOfKey = item.attribute("attrKey");
            def xmlOfValue = item.attribute("attrVal");
            def convert = item.attribute("convert");

            def packagePath = item.attribute("package");
            def inputNodeValue = sourceNode;
            def currentDTO = dto;

            //获取原始xml指定Node信息
            if (packagePath) {
                inputNodeValue = AdapterAttributeItemUtils.initLoopSourceNode(sourceNode, packagePath);
            }
            //find source xml node
            def itemInfo = inputNodeValue."$mpName"

            //处理多级别的问题
            if (classPath) {
                def subItemCls = item.attribute("cls") ? classPath + "." + item.attribute("cls") : classPath;
                currentDTO = AdapterAttributeItemUtils.initLoopClass(currentDTO, subItemCls)
            }
            //特殊的转换
            if (xmlOfKey && xmlOfValue) {
                itemInfo = itemInfo.find { it -> it.@"$xmlOfKey" == "$xmlOfValue" }
            }
            //处理
            if (itemInfo) {
                def value = itemInfo
                if (type != "json") {
                    value = value.text();
                    if (convert) {
                        value = GroovyConvertUtils."$convert"(value);
                    }
                }
                currentDTO."$dtoName" = value
            }
        }
    }


    def static convertOfItem = { ruleNode, sourceNode, dto, type ->

        ruleNode.children().each { item ->
            def mpName = item.attribute("mp");//原始xml中节点名
            def dtoName = item.attribute("stand");//获取Dto对应名
            def xmlOfKey = item.attribute("attrKey");
            def xmlOfValue = item.attribute("attrVal");
            def convert = item.attribute("convert");

            def packagePath = item.attribute("package");
            def inputNodeValue = sourceNode;
            def currentDTO = dto;

            //获取原始xml指定Node信息
            if (packagePath) {
                inputNodeValue = AdapterAttributeItemUtils.initLoopSourceNode(sourceNode, packagePath);
            }
            //find source xml node
            def itemInfo = inputNodeValue."$mpName"

            //处理多级别的问题
//            if (classPath) {
//                def subItemCls = item.attribute("cls") ? classPath + "." + item.attribute("cls") : classPath;
//                currentDTO = AdapterAttributeItemUtils.initLoopClass(currentDTO, subItemCls)
//            }
            //特殊的转换
            if (xmlOfKey && xmlOfValue) {
                itemInfo = itemInfo.find { it -> it.@"$xmlOfKey" == "$xmlOfValue" }
            }
            //处理
            if (itemInfo) {
                def value = itemInfo
                if (type != "json") {
                    value = value.text();
                    if (convert) {
                        value = GroovyConvertUtils."$convert"(value);
                    }
                }
                currentDTO."$dtoName" = value
            }
        }
    }

    /**
     * 用于获取解析XML不同的结构
     */
    def static convertXmlItem = { sourceNode, String xmlPath, String attrKey, String attrVal ->

        def inputNodeValue = AdapterAttributeItemUtils.initLoopSourceNode(sourceNode, xmlPath)
        if (null != attrKey && null != attrVal) {
            inputNodeValue = inputNodeValue.find { it -> it.@"$attrKey" == "$attrVal" }
        } else if (inputNodeValue != null && inputNodeValue.size() == 1) {
            inputNodeValue = inputNodeValue[0]
        }
        return inputNodeValue
    }
}
