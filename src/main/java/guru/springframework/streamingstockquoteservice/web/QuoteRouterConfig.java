package guru.springframework.streamingstockquoteservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_NDJSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QuoteRouterConfig {

    public static final String QUOTES_BASE_PATH = "/quotes";

    @Bean
    public RouterFunction<ServerResponse> beerRoutesV2(QuoteHandler quoteHandler){
        return route()
                .GET(QUOTES_BASE_PATH, accept(APPLICATION_JSON), quoteHandler::fetchQuotes)
                .GET(QUOTES_BASE_PATH, accept(APPLICATION_NDJSON), quoteHandler::streamQuotes)
                .build();
    }
}
