package com.dp.component


import com.alibaba.fastjson.JSON
import com.dp.component.dynamic.utils.AdapterAttributeItemUtils


/**
 * 功能描述:
 *
 *
 * @Author: yhongl
 * @Date :2018/4/27
 * @Time :13:58
 */
//def bizAdapterContext = BizAdapterContext.instance;
//def bizAdapterPO = bizAdapterContext.bizAdapterModules["$moduleType"];
//def bizAdapter = bizAdapterPO["$appId"];
def bizAdapter = new Expando()

def PATH = "F:\\Code\\Java\\Dynamic_Conversion\\src\\test\\java\\com\\dp\\component\\resources\\"
//
def fileHandler = new File(PATH + "CustomerNewJSON.xml")
def allData = fileHandler.text

def fileSourceParamsText = new File(PATH + "CustomerNewJSONParams.xml")

bizAdapter.inputMethods = { String input ->

    def sourceParams = new XmlParser().parseText(input);
//    sourceParams = JSON.parse(input)
//    def ruleNode = bizAdapter.inputRules."$methodName";
    def ruleNode = new XmlParser().parseText(allData);
    //new XmlParser().parseText(
    def allSourceRule = ruleNode["Mapping"][0].value();
    //按着level进行分组
    def allGroupValues = allSourceRule.groupBy {
        it.attribute("level")
    }.collectEntries { k, v -> [Integer.valueOf(k), v] }
    def map = new HashMap()
    allGroupValues.each { k, v ->
        if (null != v) {

            v.each { item ->
                def standName = item.attribute("stand")
                def mpName = item.attribute("mp")
                def mapNameKey = item.attribute("mpKey")
                def mapDefaultValue = item.attribute("default")
                def storageValueType = item.attribute("type")
                def listSourceID = item.attribute("listSource")
                def funName = item.attribute("function")

                //init value or value type struct
                def elementValue = null
                if (mapDefaultValue != null) {
                    elementValue = mapDefaultValue
                } else if (null == mpName && mapNameKey == null) {
                    switch (storageValueType) {
                        case "list":
                            elementValue = new ArrayList<>(1)
                            break
                        default:
                            elementValue = new HashMap<>(1)
                    }
                } else if (mapNameKey != null) {
                    def dySourceInfo = AdapterAttributeItemUtils.convertXmlItem(sourceParams, String.valueOf(mapNameKey), null, null)
                    if (null != dySourceInfo) {
                        elementValue = dySourceInfo.text()
                    } else {
                        elementValue = new HashMap<>(1)
                    }
                } else {
                    elementValue = AdapterAttributeItemUtils.initLoopSourceNode(sourceParams, String.valueOf(mpName))
                }

                elementValue = bizAdapter.funCustimze(item, elementValue, funName)

                def parentKey = item.attribute("parentKey")
                if (null == parentKey)
                    map << [(standName): elementValue]
                else if (!parentKey.contains(".") && !listSourceID)
                    map."$parentKey" << [(standName): elementValue]
                else {
                    def mapPath = parentKey.split('\\.')
                    def mapCopy = map
                    mapPath.each { nodeItem ->
                        mapCopy = mapCopy."$nodeItem"
                    }
                    mapCopy << [(standName): elementValue]

                    //list loop
                    if (listSourceID != null) {
                        bizAdapter.childResolve(null, sourceParams, item, elementValue, map, false);
                        return
                    }
                }
            }
        }
    }
    println("输出JSON格式:" + JSON.toJSONString(map))
    map
};

bizAdapter.funCustimze = { item, elementValue, funName ->
    if (!funName) {
        return elementValue
    }
    if (elementValue instanceof List || elementValue instanceof HashMap) {
        return elementValue
    }

    def appKey = item.attribute("appKey")
    def dateFmt = item.attribute("dateFmt")
    CustomizeFunCollection.instance.onAction(funName, elementValue, appKey, dateFmt);
}

bizAdapter.childResolve = { itemPre, allSourceParams, item, mpNamePath, HashMap map, Boolean sub ->


    def sourceMappingPath = String.valueOf(item.attribute("listSource"))

    def itemStandKey = String.valueOf(item.attribute("stand"))
    def itemParentPath = item.attribute("parentKey") != null ? String.valueOf(item.attribute("parentKey")).split("\\.").collect() : null
    itemParentPath << itemStandKey
    def itemIsGroup = String.valueOf(item.attribute("type"))
    def isTypeList = itemIsGroup != null && itemIsGroup.equalsIgnoreCase("list")
    def itemScan = item.attribute("scan") != null ? String.valueOf(item.attribute("scan")) : null


    def ruleMappingValues = item.value()
    if (ruleMappingValues == null)
        return

    //动态获取Source中的Stand路径
    def inputSourceParams = null
    if (itemScan != null && itemScan.equalsIgnoreCase("self")) {
        inputSourceParams = AdapterAttributeItemUtils.convertXmlItem(itemPre, sourceMappingPath, null, null)
    } else {
        inputSourceParams = AdapterAttributeItemUtils.convertXmlItem(allSourceParams, sourceMappingPath, null, null)
    }


    if (null == inputSourceParams)
        return
    def inputSourceArray = inputSourceParams
    if (inputSourceParams instanceof Node) {
        inputSourceArray = inputSourceParams.value()
    }
    def forSize = inputSourceArray.size()

    for (int i = 0; i < forSize; i++) {
        //缺少  层级的查询，指向
        def currentSourceData = inputSourceArray[i]
        def autoCreateMapStruct = mpNamePath
        if (isTypeList && autoCreateMapStruct instanceof ArrayList) {
            autoCreateMapStruct = mpNamePath[i] == null ? new HashMap() : mpNamePath[i]
        } else if (autoCreateMapStruct == null) {
            autoCreateMapStruct = new HashMap()
        } else {
//            def aa = autoCreateMapStruct[itemStandKey]
//            if (aa.size() > 0) {
//                aa[i] = new HashMap()
//            }
        }

        ruleMappingValues.each { mapping ->
            def mappingName = String.valueOf(mapping.name())

            //获取结构
            def mappingMpPath = String.valueOf(mapping.attribute("mp"))
            def mappingKeyMpPath = String.valueOf(mapping.attribute("mpKey"))

//            println("CURRENT" + "  SIZE   " + forSize + "  CURENT INDEX " + i + "   NAME:" + mappingName + "   MP  " + String.valueOf(mapping.attribute("stand")) + " SUB: " + sub.toString())
            def mappingStandKey = String.valueOf(mapping.attribute("stand"))
            def mappingIsList = mapping.attribute("type")
            def funName = mapping.attribute("function")
            def mappingAttributeKey = mapping.attribute("attrKey") != null ? String.valueOf(mapping.attribute("attrKey")) : null
            def mappingAttributeVal = mapping.attribute("attrVal") != null ? String.valueOf(mapping.attribute("attrVal")) : null
            def mappingParentPath = mapping.attribute("parentKey") != null ? String.valueOf(mapping.attribute("parentKey")) : null
            def mappingDefaultValue = mapping.attribute("default") != null ? String.valueOf(mapping.attribute("default")) : null
            if (mappingIsList != null && String.valueOf(mappingIsList).equalsIgnoreCase("list") && mappingName.equalsIgnoreCase("item")) {
                //需要处理之前已经是Iteam时的内套情况及下面 << 值的问题
                def subName = mappingParentPath.substring(mappingParentPath.lastIndexOf("\\.") + 1)
                def autoCreateMapStruct111 = autoCreateMapStruct[subName]
                if (autoCreateMapStruct111 == null) {
                    autoCreateMapStruct << [(mappingStandKey): new ArrayList(0)]
                } else {
                    if (autoCreateMapStruct111[i] != null) {
                        autoCreateMapStruct111[i] << [(mappingStandKey): new ArrayList(0)]
                    } else {
                        autoCreateMapStruct111 << [(mappingStandKey): new ArrayList(0)]
                    }
                }
//                println("LIST ITEM :" + mappingStandKey)
                //用于处理Loop foreach做用结构
                bizAdapter.childResolve(currentSourceData, allSourceParams, mapping, autoCreateMapStruct, map, true)
            } else {
                if (null == mappingDefaultValue) {
                    if (mappingMpPath) {

                        def sourceMapResult = AdapterAttributeItemUtils.convertXmlItem(currentSourceData, mappingMpPath, mappingAttributeKey, mappingAttributeVal)
                        if (null != sourceMapResult) {
                            mappingDefaultValue = sourceMapResult.text()
                        }
                    }
                    if (mappingKeyMpPath) {
                        def dySourceInfo = AdapterAttributeItemUtils.convertXmlItem(allSourceParams, mappingKeyMpPath, null, null)
                        if (null != dySourceInfo) {
                            mappingDefaultValue = dySourceInfo.text()
                        }
                    }

                }


                mappingDefaultValue = bizAdapter.funCustimze(mapping, mappingDefaultValue, funName)

                if (sub) {
                    def mapPathList = mappingParentPath.split("\\.").collect()
                    def getMapIndexPath = mappingParentPath.split("\\.").collect()[-1]
                    if (null != getMapIndexPath) {
                        def ddd = null
                        if (mapPathList.size() > 1) {
                            ddd = autoCreateMapStruct
                            mapPathList.each { nodeItem ->
                                ddd = ddd."$nodeItem"
                            }
                        } else {
                            ddd = autoCreateMapStruct[getMapIndexPath]
                        }

                        if (ddd == null) {
                            return
                        }

                        if (ddd[i] == null) {
                            ddd << [(mappingStandKey): mappingDefaultValue]
                        } else
                            ddd[i] << [(mappingStandKey): mappingDefaultValue]

                    }
                } else {
                    autoCreateMapStruct << [(mappingStandKey): mappingDefaultValue]

                }
            }

        }

        if (null != itemParentPath && !sub) {
            def mapCopy = map
            itemParentPath.each { nodeItem ->
                mapCopy = mapCopy."$nodeItem"
            }
            if (mapCopy instanceof ArrayList) {
                mapCopy << autoCreateMapStruct
            }
        }
    }
};



println(bizAdapter.inputMethods(fileSourceParamsText.text))
