package com.cloud.pay.admin.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.cloud.pay.admin.entity.User;
import com.cloud.pay.admin.util.ParameterMap;

@Component
public interface IUserService {
	public HashMap<String, Object> login(ParameterMap pm,HttpSession session);
	public String logout(HttpSession session);
	public List<ParameterMap> getUserList();
	public HashMap<String, Object> getRole(ParameterMap pm);
	public HashMap<String, Object> add(ParameterMap pm);
	public HashMap<String, Object> edit(ParameterMap pm);
	public HashMap<String, Object> editRole(ParameterMap pm);
	public HashMap<String, Object> del(ParameterMap pm);
	public User getUserInfo(ParameterMap pm);
}
