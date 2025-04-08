package com.group.commitapp.config.security;

import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		return userRepository
				.findById(Long.parseLong(id))
				.map(PrincipalDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("loadByUsername() 에서 오류났다."));
	}
}
