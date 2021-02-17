package com.boostcms.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boostcms.common.domain.Tree;
import com.boostcms.common.utils.BuildTree;
import com.boostcms.system.dao.DeptDao;
import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.service.DeptService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;




@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao sysDeptMapper;

    @Override
    public DeptDO get(Long deptId) {
        return sysDeptMapper.get(deptId);
    }

    @Override
    public List<DeptDO> list(Map<String, Object> map) {
        String deptId ="";
        if(map != null && map.containsKey("deptId")) {
        	Object deptIdObj=map.get("deptId");
        	if(deptIdObj != null) {
        		deptId=deptIdObj.toString();
        	}
        }
        if (StringUtils.isNotBlank(deptId)) {
            Long deptIdl = Long.valueOf(deptId);
            List<Long> childIds = listChildrenIds(deptIdl);
            childIds.add(deptIdl);
            map.put("deptId", null);
            map.put("deptIds",childIds);
        }
    	
    	
        return sysDeptMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
    	
        String deptId ="";
        if(map.containsKey("deptId")) {
        	Object deptIdObj=map.get("deptId");
        	if(deptIdObj != null) {
        		deptId=deptIdObj.toString();
        	}
        }
        if (StringUtils.isNotBlank(deptId)) {
            Long deptIdl = Long.valueOf(deptId);
            List<Long> childIds = listChildrenIds(deptIdl);
            childIds.add(deptIdl);
            map.put("deptId", null);
            map.put("deptIds",childIds);
        }
    	
    	
        return sysDeptMapper.count(map);
    }

    @Override
    public int save(DeptDO sysDept) {
        return sysDeptMapper.save(sysDept);
    }

    @Override
    public int update(DeptDO sysDept) {
        return sysDeptMapper.update(sysDept);
    }

    @Override
    public int remove(Long deptId) {
        return sysDeptMapper.remove(deptId);
    }

    @Override
    public int batchRemove(Long[] deptIds) {
        return sysDeptMapper.batchRemove(deptIds);
    }

    Map<Long ,Long> buildSubParentDept(List<DeptDO> sysDepts ){
 	   
 	   Map<Long ,Long> mapsubParent=new HashMap<>();
 	   for (DeptDO sysDept : sysDepts) {
 		   mapsubParent.put(sysDept.getDeptId(), sysDept.getParentId());
 	   }
 	   return mapsubParent;
     }
     
     Map<Long ,Set<Long>> buildDeptPath(List<DeptDO> sysDepts ){
     	Map<Long ,Set<Long>> mapPath=new HashMap<>();
     	 Map<Long ,Long> mapsubParent=buildSubParentDept(sysDepts);
     	for (DeptDO sysDept : sysDepts) {
     		Long curId=sysDept.getDeptId();
     		Long parentId=mapsubParent.get(curId);
     		Set<Long> pathSet=new HashSet<>();
     		while(parentId!= null) {
     			pathSet.add(parentId);
     			parentId=mapsubParent.get(parentId);
     		}
     		mapPath.put(curId, pathSet);
     	}
     	return mapPath;
     }
    
    @Override
    public Tree<DeptDO> getTree(Long deptid) {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String, Object>(16));
        Map<Long ,Set<Long>> mapPath=buildDeptPath(sysDepts );
        for (DeptDO sysDept : sysDepts) {
        	if(!mapPath.get(sysDept.getDeptId()).contains(deptid) ) {
        		if(sysDept.getDeptId() !=deptid) {
        			continue;
        		}
        	}
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(sysDept.getDeptId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DeptDO> t = BuildTree.build(trees,deptid.toString());
        return t;
    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        int result = sysDeptMapper.getDeptUserNumber(deptId);
        return result == 0 ? true : false;
    }

    @Override
    public List<Long> listChildrenIds(Long parentId) {
        List<DeptDO> deptDOS = list(null);
        return treeMenuList(deptDOS, parentId);
    }

    List<Long> treeMenuList(List<DeptDO> menuList, long pid) {
        List<Long> childIds = new ArrayList<>();
        for (DeptDO mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId() == pid) {
                //递归遍历下一级
            	childIds.addAll(treeMenuList(menuList, mu.getDeptId()));
                childIds.add(mu.getDeptId());
            }
        }
        return childIds;
    }
    
    @Override
	public Long[] listParentDept() {
		 return sysDeptMapper.listParentDept();
	}

    
	@Override
	public Map<Long,DeptDO> getAllMap(Map<String, Object> map){
		List<DeptDO> typeList=list(map);
		Map<Long,DeptDO> rest=new HashMap<>();
		for(DeptDO one:typeList) {
			rest.put(one.getDeptId(), one);
		}
		return rest;
	}
}
