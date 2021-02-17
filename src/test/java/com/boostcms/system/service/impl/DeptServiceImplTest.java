package com.boostcms.system.service.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.boostcms.system.dao.DeptDao;
import com.boostcms.system.domain.DeptDO;

import io.github.benas.randombeans.api.EnhancedRandom;


@SpringBootTest(classes = DeptServiceImpl.class)
public class DeptServiceImplTest {

	@MockBean
    private DeptDao sysDeptMapper;
	
	@InjectMocks
	private DeptServiceImpl deptServiceImpl;
	
	@BeforeEach
	public void setUp() throws Exception {
		   MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() {
        List<DeptDO> restList=new LinkedList<>();
        DeptDO deptDO=new DeptDO();
        deptDO.setDeptId(0L);
        deptDO.setName("test1");

        restList.add(deptDO);
		//String rowKey = one.getNodelabel() + TimerServiceImpl.delim + TimerServiceImpl.controller_network + TimerServiceImpl.delim + TimerServiceImpl.controller_networkname;
        Mockito.when(sysDeptMapper.list(null)).thenReturn(restList);
        
        assertEquals(deptServiceImpl.list(null),restList);
	}
	
	@Test
	public void test2() {
		EnhancedRandom easyRandom=io.github.benas.randombeans.EnhancedRandomBuilder.aNewEnhancedRandom();


		DeptDO deptDO = easyRandom.nextObject(DeptDO.class);
		
		System.out.println(easyRandom.nextObject(String.class));
		System.out.println(deptDO);
		
	}

}
