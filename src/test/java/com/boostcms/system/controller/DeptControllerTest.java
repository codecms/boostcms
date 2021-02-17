package com.boostcms.system.controller;



import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.service.DeptService;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(value = DeptController.class)
@SpringBootTest(classes = DeptController.class)
public class DeptControllerTest {

	@MockBean
	private DeptService sysDeptService;
	
	 @InjectMocks
	 private DeptController deptController; 
	
	 
	 private MockMvc mockMvc;
	 
	   @BeforeEach
	  public void setUp()
	    {
		   MockitoAnnotations.initMocks(this);
		   mockMvc = MockMvcBuilders.standaloneSetup(deptController).build();

		   
			org.apache.shiro.mgt.SecurityManager secm = new org.apache.shiro.mgt.DefaultSecurityManager();
			org.apache.shiro.SecurityUtils.setSecurityManager(secm);
			UserDO usedo = new UserDO();
			usedo.setUsername("admin");
			usedo.setDeptId(0L);
			org.apache.shiro.subject.Subject subject = org.apache.shiro.util.ThreadContext.getSubject();
			if (subject == null) {
				org.apache.shiro.subject.PrincipalCollection principals = new org.apache.shiro.subject.SimplePrincipalCollection(
						usedo, "admin");
				subject = (new org.apache.shiro.subject.Subject.Builder().principals(principals)).buildSubject();

				org.apache.shiro.util.ThreadContext.bind(subject);
			}

	    }
	
	@Test
	public void testHtml() throws Exception {
		Map<Long,DeptDO> allMap=new HashMap<>();
		Mockito.when(sysDeptService.getAllMap(null)).thenReturn(allMap);
		
		   //执行请求（使用GET请求，RESTful接口）
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/system/sysDept").accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();
 
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();
       
        System.out.println(mvcResult.getResponse().getForwardedUrl());
    
        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
 
        //断言，判断返回编码是否正确
        assertEquals(200,status);
	}
	
	
	
	
    @Test
	public void testList() throws Exception {	
	
    List<DeptDO> writefileList =new LinkedList<>();
    DeptDO oneNode=new DeptDO();
	writefileList.add(oneNode);
	Mockito.when(sysDeptService.list(Mockito.any())).thenReturn(writefileList );
	Mockito.when(sysDeptService.count(Mockito.any())).thenReturn(writefileList.size());
	//执行请求
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/system/sysDept/list").accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();
 
       //获取返回编码
      int status = mvcResult.getResponse().getStatus();
       
        System.out.println(mvcResult.getResponse().getForwardedUrl());
    
        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
 
        //断言，判断返回编码是否正确
        assertEquals(200,status);
	}
    
    @Test
    public void testSave() throws Exception {	
   
 	   Mockito.when(sysDeptService.save(Mockito.any())).thenAnswer(new Answer() {
		     public Object answer(InvocationOnMock invocation) {
		         Object[] args = invocation.getArguments();
		         DeptDO n2=(DeptDO)(args[0]);
		  	     assertEquals("100name",n2.getName());
		         return 0;
		     }
		 });	
    	
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/system/sysDept/save")
                .param("id", "1")
                .param("name", "100name")).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        
    }
    
    @Test
    public void testUpdate() throws Exception {	
   
 	   Mockito.when(sysDeptService.update(Mockito.any())).thenAnswer(new Answer() {
		     public Object answer(InvocationOnMock invocation) {
		         Object[] args = invocation.getArguments();
		         DeptDO n2=(DeptDO)(args[0]);
		  	     assertEquals("100nameupdate",n2.getName());
		         return 0;
		     }
		 });	
    	
 	  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/system/sysDept/update")
                .param("id", "1")
                .param("name", "100nameupdate")).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
    }
    
    @Test
    public void testDelete() throws Exception {	
   
 	   Mockito.when(sysDeptService.remove(Mockito.any())).thenAnswer(new Answer() {
		     public Object answer(InvocationOnMock invocation) {
		         Object[] args = invocation.getArguments();
		         Integer n2=(Integer)(args[0]);
		  	     assertEquals((Integer)103,n2);
		         return 0;
		     }
		 });	
    	
 	  MvcResult mvcResult  = mockMvc.perform(MockMvcRequestBuilders.post("/system/sysDept/remove")
                .param("id", "103")
                .param("testName", "100nameupdate")).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        
    }
    
    @Test
    public void testBatchRemove() throws Exception {	
   
 	   Mockito.when(sysDeptService.batchRemove(Mockito.any())).thenAnswer(new Answer() {
		     public Object answer(InvocationOnMock invocation) {
		         Object[] args = invocation.getArguments();
		         Long[] n2=(Long[])(args[0]);
		         Long[] expt=new Long[] {103L,104L};
		  	     assertArrayEquals(expt, n2);
		         return 0;
		     }
		 });	
    	
 	  MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/system/sysDept/batchRemove")
                .param("ids[]", "103")
                .param("ids[]", "104")
                .param("testName", "100nameupdate")).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        
    }

}
