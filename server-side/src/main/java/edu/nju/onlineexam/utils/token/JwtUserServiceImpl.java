package edu.nju.onlineexam.utils.token;

import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.type.UserType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByUsername(username);
        if (entity == null) {
            throw new UsernameNotFoundException(String.format("User not found [user=%s].", username));
        }
        List<GrantedAuthority> authorities = new ArrayList<>(3);
        if (entity.getType() == UserType.STUDENT.getCode()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
        }

        return new JwtUser(entity.getId(),username,entity.getPassword(),
            authorities,entity.getUpdateTime());

    }
}
