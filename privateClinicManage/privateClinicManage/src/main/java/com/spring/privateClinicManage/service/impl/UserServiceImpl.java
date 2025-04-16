package com.spring.privateClinicManage.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.privateClinicManage.dto.UserRegisterDto;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.oauth2.Dto.CustomOAuth2User;
import com.spring.privateClinicManage.repository.UserRepository;
import com.spring.privateClinicManage.service.RoleService;
import com.spring.privateClinicManage.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private Environment env;
	private RoleService roleService;
	private Cloudinary cloudinary;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder,
			Environment environment, RoleService roleService, Cloudinary cloudinary) {
		super();
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.env = environment;
		this.roleService = roleService;
		this.cloudinary = cloudinary;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null || !user.getActive())
			throw new UsernameNotFoundException("Không tồn tại!");
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

		return new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), authorities);
	}

	@Override
	@Transactional
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public boolean authUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		return user != null && this.encoder.matches(password, user.getPassword());
	}


	@Override
	public boolean isActived(String email) {
		User user = userRepository.findByEmail(email);
		return user.getActive();
	}

	@Override
	@Transactional
	public void saveUserRegisterDto(UserRegisterDto registerDto) {

		User user = new User();

		user.setAvatar(env.getProperty("user.avatar.default"));
		user.setName(registerDto.getName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(encoder.encode(registerDto.getPassword()));
		user.setGender(registerDto.getGender());
		user.setAddress(registerDto.getAddress());
		user.setBirthday(registerDto.getBirthday());
		user.setPhone(registerDto.getPhone());
		user.setActive(true);
		user.setRole(roleService.findByName("ROLE_BENHNHAN"));

		userRepository.save(user);
	}


	@Override
	public User getCurrentLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User user = this.findByEmail((authentication.getName()));
			return user;
		}
		return null;
	}

	@Override
	public void setCloudinaryField(User user) {
		if (!user.getFile().isEmpty()) {
			try {
				Map res = this.cloudinary.uploader().upload(user.getFile().getBytes(),
						ObjectUtils.asMap("resource_type", "auto"));
				user.setAvatar(res.get("secure_url").toString());
				user.setFile(null);
				this.userRepository.save(user);

			} catch (IOException ex) {
				Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void uploadFromUrl(User user, String imageUrl) {
		try {
			Map uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());
			user.setAvatar(uploadResult.get("secure_url").toString());
			this.userRepository.save(user);
		} catch (IOException ex) {
			Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public Page<User> findAllUserPaginated(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User findUserById(Integer userId) {
		Optional<User> optional = userRepository.findById(userId);
		if (optional.isEmpty())
			return null;
		return optional.get();
	}

	@Override
	public List<User> findUsersByRoleAndActive(Role role, Boolean active) {
		return userRepository.findUsersByRoleAndActive(role, active);
	}

	@Override
	public List<User> findByRole(Role role) {
		return userRepository.findByRole(role);
	}

	@Override
	public List<User> findByActive(Boolean active) {
		return findByActive(active);
	}

	@Override
	public List<User> findByAnyText(String key) {
		return userRepository.findByAnyText(key);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> sortByRole(List<User> users, Role role) {
		return users.stream()
				.filter(user -> user.getRole().equals(role))
				.collect(Collectors.toList());
	}

	@Override
	public List<User> sortByActive(List<User> users, String active) {
		if (active.equals("true"))
			return users.stream().filter(User::getActive).collect(Collectors.toList());
		return users.stream().filter(user -> !user.getActive()).collect(Collectors.toList());
	}

	@Override
	public Page<User> findSortedPaginateUser(Integer size, Integer page, List<User> users) {
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), users.size());

		List<User> usersPaginated;

		if (users.size() < start) {
			usersPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), users.size());
			usersPaginated = users.subList(start, end);
		}

		return new PageImpl<>(usersPaginated, pageable, users.size());

	}

	@Override
	public User findByPhone(String phone) {
		return userRepository.findByPhone(phone);
	}

	@Override
	public boolean isValidGmail(String email) {
		return email != null && email.contains("@gmail.com");
	}

	@Override
	public boolean isValidPhoneNumber(String phoneNumber) {
		String regex = "\\d{10}";
		return phoneNumber != null && phoneNumber.matches(regex);
	}

	@Override
	@Transactional
	public void processOAuthPostLogin(CustomOAuth2User customOAuth2User) {

		String email = customOAuth2User.getEmail();
		User existUser = userRepository.findByEmail(email);

		if (existUser == null) {

			String name = (String) customOAuth2User.getAttributes().get("name");
			String picture = (String) customOAuth2User.getAttributes().get("picture");
			String sub = (String) customOAuth2User.getAttributes().get("sub");

			User newUser = new User();
			newUser.setEmail(email);
			newUser.setName(name);
			newUser.setPassword(encoder.encode(sub));
			newUser.setRole(roleService.findByName("ROLE_BENHNHAN"));
			newUser.setActive(true);
			this.uploadFromUrl(newUser, picture);

			userRepository.save(newUser);
		}

	}

}
