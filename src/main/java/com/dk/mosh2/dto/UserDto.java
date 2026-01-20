package com.dk.mosh2.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
	@JsonProperty("user_id")
private Long id;
private String username;
private String email;
@JsonInclude(value = Include.NON_NULL)
private String phoneno;
//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//private LocalDateTime date;
}
