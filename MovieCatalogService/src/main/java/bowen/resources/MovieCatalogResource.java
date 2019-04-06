package bowen.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import bowen.models.CatalogItem;
import bowen.models.Movie;
import bowen.models.Rating;

@RestController
@RequestMapping("/catalogs")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		// Get all rated movie IDs
		List<Rating> ratings = Arrays.asList(
				new Rating("1", 4), 
				new Rating("2", 6)
		);
		

		return ratings.stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			/*
			Movie movie = webClientBuilder.build()
								.get()
								.uri("http://localhost:8082/movies/" + rating.getMovieId())
								.retrieve()
								.bodyToMono(Movie.class)
								.block();
			*/
			return new CatalogItem(movie.getName(), "Robot fight", rating.getRating());
		})
		.collect(Collectors.toList());
		
		
		// Put them all together
//		return Collections.singletonList(
//			new CatalogItem("Transformers", "Robot fight", 6)
//		);
	}
}
