package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			final ResponseEntity<UserOrder> response = ResponseEntity.notFound().build();
			log.info("Username not found! Please try again!", response);
			return response;
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		final ResponseEntity<UserOrder> response = ResponseEntity.ok(order);
		log.info("Submit order Successfully!", response);
		return response;
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			final ResponseEntity<List<UserOrder>> response = ResponseEntity.notFound().build();
			log.info("History not found! Please try again!", response);
			return response;
		}
		final ResponseEntity<List<UserOrder>> response = ResponseEntity.ok(orderRepository.findByUser(user));
		log.info("Get history successfully!", response);
		return response;
	}
}
