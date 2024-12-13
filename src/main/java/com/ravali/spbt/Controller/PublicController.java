package com.ravali.spbt.Controller;

import com.ravali.spbt.Model.Rating;
import com.ravali.spbt.Model.RatingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    private static final Logger log = LoggerFactory.getLogger(PublicController.class);


    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${ratings_service.url}")
    private String ratingsServiceUrl;


    @PostMapping
    public ResponseEntity<Object> addRating(@RequestBody RatingRequest ratingRequest){
        Rating rating;
        try {
            rating = restTemplate.postForObject(ratingsServiceUrl,ratingRequest, Rating.class);
            return ResponseEntity.ok(rating);
        }catch (HttpStatusCodeException exception){
                log.error("Error adding Rating: {}", exception.getMessage());
            return ResponseEntity.status(exception.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exception.getResponseBodyAsString());
        }
    }
}
