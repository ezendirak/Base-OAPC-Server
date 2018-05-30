package com.oapc.rest.auth;

import com.oapc.model.User;
import com.oapc.model.UserDTO;
import com.oapc.repo.UserRepository;
import com.oapc.rest.v2.GestioEmpressaController;
import com.oapc.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping( value = "/api", produces = MediaType.APPLICATION_JSON_VALUE )
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(GestioEmpressaController.class);
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @RequestMapping( method = GET, value = "/user/{userId}" )
    @PreAuthorize("hasRole('ADMIN')")
    public User loadById( @PathVariable Long userId ) {
        return this.userService.findById( userId );
    }

    @RequestMapping( method = GET, value= "/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
//    	List<User> test = this.userService.findAll();
//    	for (User user : test) {
//    		logger.info(user.getEmpresa().getCodi());
//		}
//    	
        return this.userService.findAll();
    }

    @RequestMapping("/whoami")
    @PreAuthorize("hasRole('USER')")
    public User user(Principal user) {
    	User test = this.userService.findByUsername(user.getName());
    	logger.info(test.getEmpresa().getCodi());

        return this.userService.findByUsername(user.getName());
    }
    
}

