package home.work.web_flux_client.stock_prices;

import home.work.grpc_server.StockPriceRequest;
import home.work.grpc_server.StockPriceResponse;
import home.work.grpc_server.StockPriceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@GrpcService
public class Stock {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 8080)
            .usePlaintext()
            .build();
    private final StockPriceServiceGrpc.StockPriceServiceStub stub = StockPriceServiceGrpc.newStub(channel);

    @Bean
    RouterFunction<ServerResponse> getStockPrice() {
        return route(GET("/stocks/{symbol}"),
                req -> ServerResponse
                        .ok()
                        .body(
                                getStockPrice(req.pathVariable("symbol")),
                                StockPrice.class
                        ));
    }

    private Flux<StockPrice> getStockPrice(String symbol) {
        var observer = new ReactiveStreamObserver<StockPrice, StockPriceResponse>() {
            @Override
            public StockPrice process(StockPriceResponse value) {
                return new StockPrice(value.getSymbol(), value.getPrice());
            }
        };
        StockPriceRequest request = StockPriceRequest.newBuilder()
                .setSymbol(symbol)
                .build();

        return observer.observe(request, stub::getPrice);
    }

    private record StockPrice(String symbol, double price) {}
}
