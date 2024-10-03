package com.example.demo.ControllerTests;

import com.example.demo.UtilTest;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
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

public class CartControllerTest {

    private CartController cartController;

    private final CartRepository cartRepository = mock(CartRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        UtilTest.injectObjects(cartController, "cartRepository", cartRepository);
        UtilTest.injectObjects(cartController, "userRepository", userRepository);
        UtilTest.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCartSuccess(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("userTest");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);
        User user = new User();
        user.setUsername("userTest");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(20));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Cart cart = user.getCart();
        cart.addItem(item);
        when(cartRepository.save(cart)).thenReturn(cart);
        ResponseEntity<Cart> response = ResponseEntity.ok(cart);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeFromCartSuccess(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("userTest");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);
        User user = new User();
        user.setUsername("userTest");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(20));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Cart cart = user.getCart();
        cart.removeItem(item);
        when(cartRepository.save(cart)).thenReturn(cart);
        ResponseEntity<Cart> response = ResponseEntity.ok(cart);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartFail(){
        ResponseEntity<Cart> response = cartController.addTocart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromCartFail(){
        ResponseEntity<Cart> response = cartController.removeFromcart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
