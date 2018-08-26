package com.cloud.pay.admin.dao;

import java.util.List;

import com.cloud.pay.admin.entity.Role;
import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.util.ParameterMap;

public interface UserDao {
	public User getUserInfo(ParameterMap pm);
	public List<Role> getUserRoleList(ParameterMap pm);
	public List<ParameterMap> getUserList();
	public void saveLoginTime(String userId);
	public void saveUser(ParameterMap pm);
	public void bathSaveUserRole(List<ParameterMap> parame);
	public void editUser(ParameterMap pm);
	public void delUser(String userId);
	public void delUserRole(String userId);
}
