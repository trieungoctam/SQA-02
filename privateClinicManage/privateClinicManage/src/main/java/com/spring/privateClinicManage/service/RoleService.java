package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.Role;

public interface RoleService {

	Role findByName(String name);

	List<Role> findAllRoles();

}
