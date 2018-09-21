package com.fire.core.dbcs.config;

/**
 * 包、元素对应信息
 * 
 * @author 
 * 
 */
public class PackageItemInfo
{
	public String PackageName;
	public String BeanName;

	public PackageItemInfo(String pkgname, String beanname)
	{
		this.PackageName = pkgname;
		this.BeanName = beanname;
	}
}
