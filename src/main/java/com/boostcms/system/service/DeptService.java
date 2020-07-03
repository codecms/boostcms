package com.boostcms.system.service;



import java.util.List;
import java.util.Map;

import com.boostcms.common.domain.Tree;
import com.boostcms.system.domain.DeptDO;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

public interface DeptService {
	
	DeptDO get(Long deptId);
	
	List<DeptDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeptDO sysDept);
	
	int update(DeptDO sysDept);
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);

	Tree<DeptDO> getTree();
	
	boolean checkDeptHasUser(Long deptId);

	List<Long> listChildrenIds(Long parentId);
	
	Long[] listParentDept();
	Map<Long,DeptDO> getAllMap(Map<String, Object> map);
}
