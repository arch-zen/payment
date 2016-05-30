package com.ymatou.payment.mybatisgenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * 修正默认生成的Model名
 * 假设表明是tb_order。默认生成的Model是TbOrder。
 * 配置该插件，将prefixes2Remove设为"tb", suffix2Append设为"PO"。
 * 则最终生成的Model名为OrderPO, Model对应的Example名为OrderExample, Mapper名为OrderMapper,
 * SqlProvider名为OrderSqlProvider.
 * 
 * @author tuwenjie
 *
 */
public class RenameModelPlugin extends PluginAdapter {
    /**
     * 这些前缀会被删除，不区分大小写
     */
    private Set<String> prefixes2Remove = new HashSet<String>();
    private String suffix2Append;

    @Override
    public boolean validate(List<String> warnings) {
        splitByComma(properties.getProperty("prefixes2Remove"), prefixes2Remove);
        suffix2Append = properties.getProperty("suffix2Append");

        if (suffix2Append != null) {
            suffix2Append = suffix2Append.trim();
            if (suffix2Append.length() == 0) {
                suffix2Append = null;
            }
        }

        return prefixes2Remove.size() > 0 || suffix2Append != null;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setExampleType(rename(introspectedTable.getExampleType(), false));
        introspectedTable.setMyBatis3JavaMapperType(rename(introspectedTable.getMyBatis3JavaMapperType(), false));
        introspectedTable.setMyBatis3SqlProviderType(rename(introspectedTable.getMyBatis3SqlProviderType(), false));

        introspectedTable.setBaseRecordType(rename(introspectedTable.getBaseRecordType(), true));
    }

    private String rename(String original, boolean applyAppend) {
        int fromIndex = original.lastIndexOf('.');
        String className = original.substring(fromIndex + 1);

        StringBuilder sb = new StringBuilder(className);
        for (String prefix : prefixes2Remove) {
            if (className.toLowerCase().startsWith(prefix.toLowerCase())) {
                sb.delete(0, prefix.length());
                break;
            }
        }
        // 首字母大写
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));


        if (applyAppend && suffix2Append != null) {
            sb.append(suffix2Append);
        }
        String newClassName = sb.toString();

        sb = new StringBuilder(original);
        sb.setLength(fromIndex + 1);
        sb.append(newClassName);

        return sb.toString();
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

}
