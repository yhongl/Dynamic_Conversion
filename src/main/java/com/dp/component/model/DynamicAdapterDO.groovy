package com.dp.component.model

/**
 * @program: Dynamic_Conversion
 *
 * @description: 用于基础对象结构存储
 *
 * @author: yhongl
 *
 * @create: 2018-05-01 17:23
 * */
class DynamicAdapterDO {


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
    def inputRules = new HashMap<String, Node>()

    /**
     * 出参结构的规则解析配置
     */
    def outputRules = new HashMap<String, Node>()

}