package com.dk.mosh2.dto.errorDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
	
	
	 private String timestamp;
	    private int status;
	    @JsonInclude(value = Include.NON_NULL)
	    private String error;
	    private String message;
	    @JsonInclude(value = Include.NON_NULL)
	    private String path;
	    
	    @JsonInclude(value = Include.NON_NULL)
	    // Optional: a list of specific field errors, common in validation failures
	    private List<FieldErrorDto> fieldErrors;
}
