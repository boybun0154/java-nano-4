package com.example.demo.ControllerTests;

import com.example.demo.UtilTest;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author HuyHDN2
 * birthDay 10/22/2002
 * at 10/1/2024
 */
public class OrderControllerTest {

    private OrderController orderController;

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        UtilTest.injectObjects(orderController, "orderRepository", orderRepository);
        UtilTest.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void testSubmitSuccess(){
        User user = new User();
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(Collections.emptyList());
        user.setCart(cart);
        user.setUsername("testUserName");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrdersForUserSuccess(){
        User user = new User();
        user.setUsername("testUserName");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(Collections.emptyList());
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSubmitFail(){
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrdersForUserFail(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
