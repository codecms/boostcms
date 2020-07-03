package com.boostcms.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.jm.dataqulity.domain.EntityTypeDO;
//import com.jm.dataqulity.domain.EntityTypeEnum;
//import com.jm.dataqulity.domain.EnumInterface;

 //enum EntityTypeEnum  {
//	Unknown(0,"未知");
//}

@Service
public class BuildEnumFromDB {
	
	//@Autowired
	//private EntityTypeService entityTypeService;

	public static void main(String[] args) {
	/*	
		Constructor<?>[] list=EntityTypeEnum.class.getDeclaredConstructors();
		
		Method[] meth= EntityTypeEnum.class.getDeclaredMethods();
		EntityTypeEnum a=EntityTypeEnum.Unknown;
		
        DynamicEnumUtils.addEnum(EntityTypeEnum.class, "JiaoQuJianCeDian",new Class<?>[]{int.class, String.class}, new Object[]{1,"郊区县监测点"});
	    
        EntityTypeEnum d1=EntityTypeEnum.valueOf("JiaoQuJianCeDian");
        System.out.println(d1.getName());
        
        System.out.println(EntityTypeEnum.getInstance(1));
   */     
      //  EntityTypeEnum d2=EntityTypeEnum.valueOf("JiaoQuJian");
	}
	
	public void LoadDbIntoEnum() {
		
		//Map<String, Object> map=new HashMap<>();
		//List<EntityTypeDO> li=entityTypeService.list(map);
		
		//for(EnumInterface one:li) {
		//	DynamicEnumUtils.addEnum(EntityTypeEnum.class,one.getCode(),
		//			new Class<?>[]{int.class, String.class}, new Object[]{one.getId(),one.getName()});
		//} 
	}
}
