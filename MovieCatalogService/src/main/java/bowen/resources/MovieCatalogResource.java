package bowen.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bowen.models.CatalogItem;
import bowen.models.Movie;
import bowen.models.UserRating;

@RestController
@RequestMapping("/catalogs")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
//	@Autowired
//	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		// Get all rated movie IDs
		UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
		

		return userRating.getUserRating().stream().map(rating -> {
			// For each movie ID, call movie info service and get movie details
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);		
			/*
			Movie movie = webClientBuilder.build()
								.get()
								.uri("http://localhost:8082/movies/" + rating.getMovieId())
								.retrieve()
								.bodyToMono(Movie.class)
								.block();
			*/
			
			// Put them all together
			return new CatalogItem(movie.getName(), "Robot fight", rating.getRating());
		})
		.collect(Collectors.toList());

	}
}
