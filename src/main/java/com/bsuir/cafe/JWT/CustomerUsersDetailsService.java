package com.bsuir.cafe.JWT;

import com.bsuir.cafe.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    private com.bsuir.cafe.POJO.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail)){
            return new User(userDetail.getName(), userDetail.getPassword(), new ArrayList<>());

        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

    public com.bsuir.cafe.POJO.User getUserDetail(){
       /* com.bsuir.cafe.POJO.User user = userDetail;
        user.setPassword(null);*/
        return userDetail;
    }
}
