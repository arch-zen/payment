/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Map工具类
 * 
 * @author wangxudong 2016年5月22日 下午3:07:57
 *
 */
public final class MapUtil {

    /**
     * 把XML String转换成MAP
     * 
     * @param xmlString
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Map<String, String> parseFromXml(String xmlString)
            throws ParserConfigurationException, SAXException, IOException {
        // 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = getStringStream(xmlString);
        Document document = builder.parse(is);

        // 获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, String> map = new HashMap<String, String>();
        int i = 0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if (node instanceof Element) {
                map.put(node.getNodeName(), node.getTextContent());
            }
            i++;
        }
        return map;
    }

    /**
     * 获取字符串输入流
     * 
     * @param inputString
     * @return
     */
    private static InputStream getStringStream(String inputString) {
        ByteArrayInputStream inputStringStream = null;
        if (inputString != null && !inputString.trim().equals("")) {
            inputStringStream = new ByteArrayInputStream(inputString.getBytes());
        }
        return inputStringStream;
    }
}
