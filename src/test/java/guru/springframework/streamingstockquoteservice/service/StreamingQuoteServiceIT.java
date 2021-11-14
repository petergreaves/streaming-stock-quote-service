package guru.springframework.streamingstockquoteservice.service;

import guru.springframework.streamingstockquoteservice.model.Quote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CountDownLatch;

import static guru.springframework.streamingstockquoteservice.web.QuoteRouterConfig.QUOTES_BASE_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class StreamingQuoteServiceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Happy path test for fixed quotes list")
    public void testFetchQuotes() {

        webTestClient
                .get()
                .uri(QUOTES_BASE_PATH+"?size=20")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Quote.class)
                .hasSize(20)
                .consumeWith(listEntityExchangeResult -> {
                    assertThat(listEntityExchangeResult.getResponseBody()).hasSize(20);
                    assertThat(listEntityExchangeResult.getResponseBody()).allSatisfy(q -> assertThat(q.getPrice()).isPositive());
                    assertThat(listEntityExchangeResult.getResponseBody()).allSatisfy(q -> assertThat(q.getTicker()).isNotNull());
                });
    }



    @Test
    @DisplayName("Stream quotes")
    public void streamQuotes() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        webTestClient
                .get()
                .uri(QUOTES_BASE_PATH)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .returnResult(Quote.class)
                .getResponseBody()
                .take(10)
                .subscribe( quoteFluxExchangeResult ->{
                   assertThat(quoteFluxExchangeResult.getPrice()).isPositive();
                   countDownLatch.countDown();
                });

        countDownLatch.await();


    }





    @Test
    @DisplayName("10 quotes are returned by def if no size QS param")
    public void testFetchQuotesSizeTest() {

        webTestClient
                .get()
                .uri(QUOTES_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Quote.class)
                .hasSize(10)
                .consumeWith(listEntityExchangeResult -> {
                    assertThat(listEntityExchangeResult.getResponseBody()).hasSize(10);
                                });
    }


    @Test
    @DisplayName("Context loads")
    public void contextLoads() {}

}
