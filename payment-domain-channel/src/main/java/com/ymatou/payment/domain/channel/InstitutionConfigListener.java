package com.ymatou.payment.domain.channel;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.DisConf;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

/**
 * 第三方机构配置文件监听
 * 
 * @author wangxudong
 *
 */
@DisconfUpdateService(confFileKeys = { "institution.xml" })
@Component
public class InstitutionConfigListener implements IDisconfUpdate {

	private final String CONFIG_FILE = "institution.xml";

	/**
	 * 第三方机构配置列表
	 */
	private List<InstitutionConfig> institutionConfigList;

	/**
	 * 重新加载配置文件
	 */
	@Override
	public void reload() throws Exception {
		init();

	}

	/**
	 * 初始化配置文件
	 */
	private void init() {
		if (institutionConfigList != null)
			return;
		try {
			File configFile = DisConf.getLocalConfig(CONFIG_FILE);
			JAXBContext context = JAXBContext.newInstance(InstitutionConfigListener.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			InstitutionConfigListener instConfigContainer = (InstitutionConfigListener) unmarshaller.unmarshal(new FileReader(configFile));

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 根据PayType获取到第三方机构信息
	 * 
	 * @param payType
	 * @return
	 */
	public InstitutionConfig getInstitutionConfig(String payType) {
		init();

		for (InstitutionConfig institutionConfig : institutionConfigList) {
			if (institutionConfig.getPayType().equals(payType))
				return institutionConfig;
		}
		return null;
	}

}
