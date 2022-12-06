package com.cgd.coffee_machine.dto;

import com.cgd.coffee_machine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data @AllArgsConstructor
public class JwtResponse implements Serializable {

	private final String token;
	private User user;

}