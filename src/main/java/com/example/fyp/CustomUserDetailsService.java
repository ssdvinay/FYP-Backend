package com.example.fyp;

import com.example.fyp.entity.Dealer;
import com.example.fyp.entity.User;
import com.example.fyp.repository.DealerRepository;
import com.example.fyp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DealerRepository dealerRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, DealerRepository dealerRepository) {
        this.userRepository = userRepository;
        this.dealerRepository = dealerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new UsernameNotFoundException("Username not found");
        Dealer dealer = null;
        if (user.getRole().equals("DEALER"))
            dealer = dealerRepository.findDealerById(user.getId());
        return new CustomUserDetails(user, dealer);
    }
}