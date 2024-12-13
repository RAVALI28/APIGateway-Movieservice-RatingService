package com.ravali.spbt.Controller;

import com.ravali.spbt.Model.Movie;
import com.ravali.spbt.Model.MovieRating;
import com.ravali.spbt.Model.Rating;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${movie_springboot_microservices_example.url}")
    private String movieServiceUrl;
    
    @Value("${ratings_service.url}")
    private String ratingsServiceUrl;

    @PostMapping
    public ResponseEntity<Object> addMovie(@RequestBody Movie movie){
        try {
            log.info("Adding Movie");
            Movie savedMovie = restTemplate.postForObject(movieServiceUrl, movie, Movie.class);
            return ResponseEntity.ok().body(savedMovie);
        } catch (HttpStatusCodeException ex){
            log.error("Error adding movie: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable Long id, @RequestBody Movie movie){
        try{
            log.info("Updating movie: {}" , id);
            restTemplate.put(movieServiceUrl + "/" +id, movie);
            return ResponseEntity.ok().build();
        } catch (HttpStatusCodeException ex) {
            log.error("Error updsating movie: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchMovieAndRating(@PathVariable Long id){
        Movie movie = new Movie();

       try {
           movie = restTemplate.getForObject(movieServiceUrl + "/" + id, Movie.class);
       }
       catch (HttpStatusCodeException exception){
           log.error("Error fetching movie : {}", exception.getMessage());
           ResponseEntity.status(exception.getStatusCode())
                   .contentType(MediaType.APPLICATION_JSON)
                   .body(exception.getResponseBodyAsString());
       }

       Rating rating;

        try{
           rating = restTemplate.getForObject(ratingsServiceUrl + "/" + movie.getName(), Rating.class);
       }
       catch (HttpStatusCodeException exception){

          if(exception.getStatusCode() == HttpStatus.NOT_FOUND){
              rating = new Rating(null, movie.getName(), 0.0,0 );
          }
          else {
              rating = new Rating(null, movie.getName(), -1.0, -1);
          }
       }catch (ResourceAccessException ex){
            log.warn("Exception:", ex.getMessage());
            rating = new Rating(null, movie.getName(), -1.0, -1);

        }

        MovieRating movieRating = new MovieRating();
        movieRating.setMovie(movie);
        movieRating.setRating(rating);

        return ResponseEntity.ok(movieRating);

    }
    
}
