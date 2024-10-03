package com.example.demo.ControllerTests;

import com.example.demo.UtilTest;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author HuyHDN2
 * birthDay 10/22/2002
 * at 10/1/2024
 */
public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        UtilTest.injectObjects(userController, "userRepository", userRepository);
        UtilTest.injectObjects(userController, "cartRepository", cartRepository);
        UtilTest.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testFindByUserName() throws Exception{

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("Test123123@");
        createUserRequest.setConfirmPassword("Test123123@");

        final ResponseEntity<User> responseUser = userController.createUser(createUserRequest);
        User user = responseUser.getBody();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", user.getUsername());
    }

    @Test
    public void testFindByID() throws Exception{

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("Test123123@");
        createUserRequest.setConfirmPassword("Test123123@");

        final ResponseEntity<User> responseUser = userController.createUser(createUserRequest);
        User user = responseUser.getBody();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(user.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, user.getId());
    }

    @Test
    public void testCreateUserSuccess() throws Exception{
        when(bCryptPasswordEncoder.encode("Test123123@")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("Test123123@");
        createUserRequest.setConfirmPassword("Test123123@");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(user.getId(), 0);
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void testCreateUserFail() throws Exception{
        when(bCryptPasswordEncoder.encode("Test123")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("Test123");
        createUserRequest.setConfirmPassword("Test123");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

}
