package guru.springframework.streamingstockquoteservice.service;

import guru.springframework.streamingstockquoteservice.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class QuoteGeneratorServiceImplTest {


    QuoteGeneratorService quoteGeneratorService;
    @BeforeEach
    void setUp() {

        quoteGeneratorService = new QuoteGeneratorServiceImpl();
    }


    @Test
    public void fetchQuoteStream() throws Exception {
        Flux<Quote> quotesFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        Consumer<Quote> quoteConsumer = System.out::println;
        Consumer<Throwable> throwableConsumer = e -> System.out.println(e.getMessage());

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runnable done = () -> countDownLatch.countDown();

        quotesFlux.take(30)
                .subscribe(quoteConsumer, throwableConsumer, done);

        countDownLatch.await();

    }
}