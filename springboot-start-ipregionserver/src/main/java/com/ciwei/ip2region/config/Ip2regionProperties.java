package com.ciwei.ip2region.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ip2region 配置类
 *
 * @author FuHang
 */
@ConfigurationProperties(prefix = "ciwei.ip2region")
public class Ip2regionProperties {

	/**
	 * ip2region.db 文件路径
	 */
	private String dbFileLocation = "classpath:ip2region/ip2region.db";

	public String getDbFileLocation() {
		return dbFileLocation;
	}

	public void setDbFileLocation(String dbFileLocation) {
		this.dbFileLocation = dbFileLocation;
	}
}
