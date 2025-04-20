package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.privateClinicManage.dto.UserRegisterDto;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.oauth2.Dto.CustomOAuth2User;


public interface UserService extends UserDetailsService {

	User findByEmail(String email);

	void saveUser(User user);

	void saveUserRegisterDto(UserRegisterDto registerDto);

	boolean authUser(String email, String password);

	User getCurrentLoginUser();

	void setCloudinaryField(User user);

	Page<User> findAllUserPaginated(Pageable pageable);

	User findUserById(Integer userId);

	List<User> findUsersByRoleAndActive(Role role, Boolean active);

	List<User> findByRole(Role role);

	List<User> findByActive(Boolean active);

	List<User> findByAnyText(String key);

	List<User> findAllUsers();

	List<User> sortByRole(List<User> users, Role role);

	List<User> sortByActive(List<User> users, String active);

	Page<User> findSortedPaginateUser(Integer size, Integer page, List<User> users);

	boolean isActived(String email);

	User findByPhone(String phone);

	boolean isValidGmail(String email);

	boolean isValidPhoneNumber(String phoneNumber);

	void processOAuthPostLogin(CustomOAuth2User customOAuth2User);

	void uploadFromUrl(User user, String imageUrl);

}
