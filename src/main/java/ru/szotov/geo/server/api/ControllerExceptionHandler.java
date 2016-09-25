package ru.szotov.geo.server.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleValidationException(ConstraintViolationException exception) {
		
		ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDateTime(new Date().getTime());
        errorMessage.setException(exception.getClass().getSimpleName());
        List<String> messages = new ArrayList<>(exception.getConstraintViolations().size());
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            Iterator<Node> iterator = constraintViolation.getPropertyPath().iterator();
            messages.add(String.format("%s: %s", 
            		iterator.next().getName(), constraintViolation.getMessage()));
        }
        errorMessage.setMessage(messages.stream().collect(Collectors.joining(", ")));
        
        return errorMessage;
	}

}
