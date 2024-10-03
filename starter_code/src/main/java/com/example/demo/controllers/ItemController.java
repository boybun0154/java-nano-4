package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.persistence.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		final ResponseEntity<List<Item>> response = ResponseEntity.ok(itemRepository.findAll());
		log.info("Get all items successfully!", response);
		return response;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		final ResponseEntity<Item> response = ResponseEntity.of(itemRepository.findById(id));
		log.info("Get item successfully!", response);
		return response;
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if(items == null || items.isEmpty()) {
			final ResponseEntity<List<Item>> response = ResponseEntity.notFound().build();
			log.info("Get list items fail! Please try again!", response);
			return response;
		}
		final  ResponseEntity<List<Item>> response = ResponseEntity.ok(items);
		log.info("Get list items Successfully!", response);
		return response;
	}
	
}
