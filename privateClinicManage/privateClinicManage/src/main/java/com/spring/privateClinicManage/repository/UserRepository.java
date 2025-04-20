package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;

@Repository
public interface UserRepository
		extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {

	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.role = :role and u.active = :active ")
	List<User> findUsersByRoleAndActive(@Param("role") Role role, @Param("active") Boolean active);

	List<User> findByRole(Role role);

	List<User> findByActive(Boolean active);

	@Query("SELECT u FROM User u WHERE " +
			"u.name LIKE %:key% OR " +
			"u.phone LIKE %:key% OR " +
			"u.email LIKE %:key% OR " +
			"u.address LIKE %:key% ")
	List<User> findByAnyText(@Param("key") String key);

	User findByPhone(String phone);
}
