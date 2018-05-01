package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 *
 * @description: 用于适配对象的数据模型组合
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:26
 * */
class DynamicAdapterDefaultDO {

    /**
     * 入参执行的Groovy脚本
     */
    def inputMethods = new Expando()

    /**
     * 出参执行的Groovy脚本
     */
    def outputMethods = new Expando()

    /**
     * 入参结构的规则解析配置
     */
    def inputRules = new Node()

    /**
     * 出参结构的规则解析配置
     */
    def outputRules = new Node()

}