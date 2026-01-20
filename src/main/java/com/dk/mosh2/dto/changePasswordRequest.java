package com.dk.mosh2.dto;

import lombok.Data;

@Data
public class changePasswordRequest {
private String oldpassword;
private String newpassword;
}
