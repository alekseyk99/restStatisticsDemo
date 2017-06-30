package com.alekseyk99.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebController {
	
   Service data;
   private static final Logger logger = LoggerFactory.getLogger(WebController.class);

   
   @Autowired
   public WebController(Service data) {
	   this.data = data;
   }

   @RequestMapping(value="/transactions", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> transaction(@RequestBody Transaction transaction) throws Exception {
	   logger.debug("POST transactions");
   
	   if (transaction ==null) throw new IllegalArgumentException("transaction can't be null");
	   
	   logger.info(transaction.toString());
    	
	   if (transaction.getAmount() == 0) throw new IllegalArgumentException("amount can't be 0");
	   if (transaction.getTimestamp() == 0) throw new IllegalArgumentException("timestamp can't be 0");
			   
       data.addTransaction(transaction);
       return new ResponseEntity<String>(HttpStatus.CREATED);
   }

   @RequestMapping(value="/statistics", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public Statistic statistics() {
	   
	    logger.debug("GET statistics");
	   
    	Statistic statistic = data.getStatistic();
    	logger.info(statistic.toString());
    	
    	return statistic;
    }

   @ExceptionHandler(IllegalArgumentException.class)
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void handleIllegalArgumentException(IllegalArgumentException exception) {
	   logger.error(exception.toString());
   }
   
   @ExceptionHandler(HttpMessageNotReadableException.class)
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
	   logger.error(exception.toString());
   }
   
   @ExceptionHandler
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void handleException(Exception exception) {
	   logger.error("Exception: ",exception);
   }
   
}