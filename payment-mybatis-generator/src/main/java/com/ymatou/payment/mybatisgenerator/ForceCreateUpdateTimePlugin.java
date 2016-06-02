package com.ymatou.payment.mybatisgenerator;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 表中一般有两列：
 * 插入时间：例如insert_time
 * 最后更新时间：例如last_update_time
 * 
 * 该插件使得：
 * 自动生成的insert语句：insert_time & last_update_time总是填入数据库当前时间
 * 自动生成的update语句：不会包含insert_time列，last_update_time总是填入数据库当前时间
 * 
 * 所有表中的插入时间列名，配置在insertTimeColumns属性，以','分隔
 * 所有表中的最后更新时间列名，配置lastUpdateTimeColumns属性，以','分隔
 * 数据库当前时间表达式，配置dbCurrentTimeExpr
 * 
 * 示例
 * <plugin type="com.ymatou.tradeservice.mybatisgenerator.ForceCreateUpdateTimePlugin">
 * <property name="insertTimeColumns" value="INSERTTIME,CREATETIME,GMT_CREATE" />
 * <property name="lastUpdateTimeColumns" value="LASTUPDATETIME,UPDATETIME,GMT_UPDATE" />
 * <property name="dbCurrentTimeExpr", value="getdate()"
 * </plugin>
 * 
 * @author tuwenjie
 *
 */
public class ForceCreateUpdateTimePlugin extends PluginAdapter {

    private Set<String> insertTimeColumnNames = new HashSet<String>();

    private Set<String> lastUpdateTimeColumnNames = new HashSet<String>();


    private String dbCurrentTimeExpr;

    @Override
    public boolean validate(List<String> warnings) {
        splitByComma(properties.getProperty("insertTimeColumns"), insertTimeColumnNames);
        splitByComma(properties.getProperty("lastUpdateTimeColumns"), lastUpdateTimeColumnNames);
        dbCurrentTimeExpr = properties.getProperty("dbCurrentTimeExpr");
        return insertTimeColumnNames.size() > 0
                || lastUpdateTimeColumnNames.size() > 0 && dbCurrentTimeExpr != null && dbCurrentTimeExpr.length() > 0;
    }

    private void splitByComma(String original, Set<String> result) {
        if (original != null) {
            String[] splits = original.split(",");
            for (String split : splits) {
                String trimmed = split.trim();
                if (trimmed.length() > 0) {
                    result.add(trimmed);
                }
            }
        }
    }



    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

        int i = 0;
        for (Element child : element.getElements()) {
            if (child instanceof TextElement) {
                TextElement old = (TextElement) child;
                String content = old.getContent();
                for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{" + insertTimeJavaProperty + "[^\\{]*\\}", dbCurrentTimeExpr);
                }
                for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{" + updateTimeJavaProperty + "[^\\{]*\\}", dbCurrentTimeExpr);
                }

                element.getElements().set(i, new TextElement(content));

            }
            i++;
        }
        return true;
    }



    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        int i = 0;
        for (Element child : element.getElements()) {
            if (child instanceof XmlElement) {
                XmlElement xe = (XmlElement) child;
                int j = 0;

                for (Attribute attr : xe.getAttributes()) {
                    String newValue = attr.getValue();

                    for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                        newValue = newValue.replaceAll(insertTimeJavaProperty + " != null", "true");
                    }
                    for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                        newValue = newValue.replaceAll(updateTimeJavaProperty + " != null", "true");
                    }

                    xe.getAttributes().set(j, new Attribute(attr.getName(), newValue));
                    j++;
                }

                // recursive
                sqlMapInsertSelectiveElementGenerated((XmlElement) child, introspectedTable);

            } else {
                TextElement old = (TextElement) child;
                String content = old.getContent();
                for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{" + insertTimeJavaProperty + "[^\\{]*\\}", dbCurrentTimeExpr);
                }
                for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{" + updateTimeJavaProperty + "[^\\{]*\\}", dbCurrentTimeExpr);
                }


                ((XmlElement) element).getElements().set(i, new TextElement(content));

            }
            i++;

        }
        return true;
    }



    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        int i = 0;
        for (Element child : element.getElements()) {
            if (child instanceof TextElement) {
                TextElement old = (TextElement) child;
                String content = old.getContent();
                for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                    if (content.indexOf(insertTimeJavaProperty) >= 0) {
                        content = "";
                    }
                }
                for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{[^\\{]*" + updateTimeJavaProperty + "[^\\{]*\\}",
                            dbCurrentTimeExpr);
                }

                element.getElements().set(i, new TextElement(content));

            }
            i++;
        }
        return true;
    }



    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }



    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        int i = 0;
        for (Element child : element.getElements()) {
            if (child instanceof XmlElement) {
                XmlElement xe = (XmlElement) child;
                int j = 0;

                for (Attribute attr : xe.getAttributes()) {
                    String newValue = attr.getValue();

                    for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                        newValue = newValue.replaceAll("record." + insertTimeJavaProperty + " != null", "false");
                        newValue = newValue.replaceAll(insertTimeJavaProperty + " != null", "false");

                    }
                    for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                        newValue = newValue.replaceAll("record." + updateTimeJavaProperty + " != null", "true");
                        newValue = newValue.replaceAll(updateTimeJavaProperty + " != null", "true");
                    }

                    xe.getAttributes().set(j, new Attribute(attr.getName(), newValue));
                    j++;
                }

                // recursive
                sqlMapUpdateByPrimaryKeySelectiveElementGenerated((XmlElement) child, introspectedTable);

            } else {
                TextElement old = (TextElement) child;
                String content = old.getContent();
                for (String insertTimeJavaProperty : getInsertTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{[^\\{]*" + insertTimeJavaProperty + "[^\\{]*\\}",
                            dbCurrentTimeExpr);
                }
                for (String updateTimeJavaProperty : getLastUpdateTimeJavaProperties(introspectedTable)) {
                    content = content.replaceAll("#\\{[^\\{]*" + updateTimeJavaProperty + "[^\\{]*\\}",
                            dbCurrentTimeExpr);
                }


                ((XmlElement) element).getElements().set(i, new TextElement(content));

            }
            i++;

        }
        return true;
    }



    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }



    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }


    private Set<String> getMatchedProperty(IntrospectedTable introspectedTable, Set<String> columnNames) {
        Set<String> result = new HashSet<String>();
        for (String column : columnNames) {
            String property = getJavaProperty(column, introspectedTable);
            if (property != null) {
                result.add(property);
            }
        }
        return result;
    }

    private Set<String> getInsertTimeJavaProperties(IntrospectedTable introspectedTable) {
        return getMatchedProperty(introspectedTable, insertTimeColumnNames);
    }

    private Set<String> getLastUpdateTimeJavaProperties(IntrospectedTable introspectedTable) {
        return getMatchedProperty(introspectedTable, lastUpdateTimeColumnNames);
    }



    private String getJavaProperty(String columnName, IntrospectedTable introspectedTable) {
        IntrospectedColumn column = introspectedTable.getColumn(columnName);
        return column == null ? null : column.getJavaProperty();
    }

    public static void main(String[] args) {
        System.out.println("#{inserttime,jdbcType=TIMESTAMP},#{ }".replaceAll(
                "#\\{inserttime[^\\{]*\\}", "getdate()"));
        System.out.println("addTime != null".replaceAll(
                "addTime != null", "true"));
        System.out.println("record.addTime != null".replaceAll(
                "record.addTime != null", "true"));
    }

}
