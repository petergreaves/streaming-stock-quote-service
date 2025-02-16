package guru.springframework.streamingstockquoteservice.web;

import guru.springframework.streamingstockquoteservice.model.Quote;
import guru.springframework.streamingstockquoteservice.service.QuoteGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
@Slf4j
@RequiredArgsConstructor
public class QuoteHandler {

    private final QuoteGeneratorService quoteGeneratorService;

    public Mono<ServerResponse> fetchQuotes(ServerRequest serverRequest) {

        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L))
                        .take(size), Quote.class);
    }

    public Mono<ServerResponse> streamQuotes(ServerRequest serverRequest) {

        return ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L))
                        , Quote.class);

    }
}
