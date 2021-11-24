package com.ds.tools.util.mycat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaLoaderUtil {
	/**
	 * 解析配置文件
	 * @return
	 */
    public static List<DataNode> getDataNodeList() {
        Map<String, DataHost> dataHostMap = new HashMap<>();
        List<DataNode> dataNodeList = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        InputStream in = null;
        try {
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            in = SchemaLoaderUtil.class.getClassLoader().getResourceAsStream("schema.xml");
            Document document = documentBuilder.parse(in);
            Element rootElement = document.getDocumentElement();
            //获取dataHost
            NodeList dataHostNList = rootElement.getElementsByTagName("dataHost");
            for (int i = 0; i < dataHostNList.getLength(); i++) {
                Element dataHostEl = (Element) dataHostNList.item(i);
                DataHost dataHost = wrappDataHost(dataHostEl);
                dataHostMap.put(dataHost.getName(), dataHost);
            }
            //获取dataNode
            NodeList dataNodeNList = rootElement.getElementsByTagName("dataNode");
            for (int i = 0; i < dataNodeNList.getLength(); i++) {
                Element dataHostEl = (Element) dataNodeNList.item(i);
                List<DataNode> dataNodeListItem = wrappDataNode(dataHostEl, dataHostMap);
                dataNodeList.addAll(dataNodeListItem);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataNodeList;

    }

    public static DataHost wrappDataHost(Element dataHostEl) {
        DataHost dataHost = new DataHost();
        dataHost.setName(dataHostEl.getAttribute("name"));
        NodeList writeHostNode = dataHostEl.getElementsByTagName("writeHost");
        if (writeHostNode.getLength() > 0) {
            Element writeHostEl = (Element) writeHostNode.item(0);
            dataHost.setWriteHostUrl(writeHostEl.getAttribute("url"));
            dataHost.setUserName(writeHostEl.getAttribute("user"));
            dataHost.setPassword(writeHostEl.getAttribute("password"));
            NodeList readNodeList = writeHostEl.getElementsByTagName("readHost");
            if (readNodeList.getLength() > 0) {
                Element readHostEl = (Element) readNodeList.item(0);
                dataHost.setReadHostUrl(readHostEl.getAttribute("url"));
            }
        }
        return dataHost;
    }

    public static List<DataNode> wrappDataNode(Element dataNodeEl, Map<String, DataHost> dataHostMap) {
        List<DataNode> dataNodeList = new ArrayList<>();
        String nameM = dataNodeEl.getAttribute("name");
        String databaseNameM = dataNodeEl.getAttribute("database");
        String dataHost = dataNodeEl.getAttribute("dataHost");
        if (nameM.equals("dndb0")) {
            DataNode dataNode = new DataNode();
            dataNode.setName(nameM);
            dataNode.setDbName(databaseNameM);
            dataNode.setDataHost(dataHostMap.get(dataHost));
            dataNodeList.add(dataNode);
            return dataNodeList;
        }
        String namePre = nameM.split("\\$")[0];
        String databaseNameMPre = databaseNameM.split("\\$")[0];
        String numScope = nameM.substring(nameM.indexOf("$") + 1);
        String[] dbBanners = numScope.split("-");
        int startN = Integer.valueOf(dbBanners[0]);
        int endN = Integer.valueOf(dbBanners[1]);
        for (int i = startN; i <= endN; i++) {
            DataNode dataNode = new DataNode();
            dataNode.setName(namePre + i);
            dataNode.setDbName(databaseNameMPre + i);
            dataNode.setDataHost(dataHostMap.get(dataHost));
            dataNodeList.add(dataNode);
        }
        return dataNodeList;
    }
}
