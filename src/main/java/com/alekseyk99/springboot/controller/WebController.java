package com.alekseyk99.springboot.controller;

import javax.validation.Valid;
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
import com.alekseyk99.springboot.dto.Statistic;
import com.alekseyk99.springboot.dto.Transaction;
import com.alekseyk99.springboot.service.TransactionStatisticsService;

/**
 * Handles requests "/transactions" and "/statistics"
 */
@RestController
public class WebController {
	
   TransactionStatisticsService service;
   private static final Logger logger = LoggerFactory.getLogger(WebController.class);

   
   @Autowired
   public WebController(TransactionStatisticsService service) {
	   this.service = service;
   }

   @RequestMapping(value="/transactions", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> transaction(@RequestBody Transaction transaction) throws Exception {

	   logger.info("POST transactions [{}]", transaction.toString());
    	
	   if (transaction.getAmount() == 0) throw new IllegalArgumentException("amount can't be 0");
	   if (transaction.getTimestamp() == 0) throw new IllegalArgumentException("timestamp can't be 0");
			   
       service.addTransaction(transaction);
       return new ResponseEntity<String>(HttpStatus.CREATED);
   }

   @RequestMapping(value="/statistics", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public Statistic statistics() {
	   
    	Statistic statistic = service.getStatistic();
    	logger.info("GET statistics [{}]", statistic.toString());
    	
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