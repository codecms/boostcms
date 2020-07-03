package com.boostcms.common.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

public class BaseDomain  {

	

	  @Override 
      public String toString() { 
              return ReflectionToStringBuilder.toString(this); 
      }
	  
		@Override
		public int hashCode() {
			     return HashCodeBuilder.reflectionHashCode(this);
		}
		
		
		@Override
		public boolean equals(Object obj) {
			return  EqualsBuilder.reflectionEquals(this, obj);
		}
	   
		///
		 public int compareTo(Object obj) {
               return CompareToBuilder.reflectionCompare(this, obj);
       }
	  
		 
			public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {  
			    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
			    ObjectOutputStream out = new ObjectOutputStream(byteOut);  
			    out.writeObject(src);  
			  
			    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
			    ObjectInputStream in = new ObjectInputStream(byteIn);  
			    @SuppressWarnings("unchecked")  
			    List<T> dest = (List<T>) in.readObject();  
			    return dest;  
			}  
}
