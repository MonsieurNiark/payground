package fr.purse.payground.internal.control;

import fr.purse.payground.control.OrderControlService;
import fr.purse.payground.control.PaymentControlService;
import fr.purse.payground.control.PaymentOrderControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.dto.request.RequestUpdatePaymentStatusDto;
import fr.purse.payground.dto.response.ResponseOrderDto;
import fr.purse.payground.dto.response.ResponsePaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of PaymentOrderControlService
 */
@Service
public class PaymentOrderControlServiceImpl implements PaymentOrderControlService {

    private final PaymentControlService paymentControlService;
    private final OrderControlService orderControlService;

    /**
     * Constructor
     *
     * @param paymentControlServiceParam control service of Payment
     * @param orderControlServiceParam   control service of Order
     */
    @Autowired
    public PaymentOrderControlServiceImpl(PaymentControlService paymentControlServiceParam,
                                          OrderControlService orderControlServiceParam) {
        this.paymentControlService = paymentControlServiceParam;
        this.orderControlService = orderControlServiceParam;

    }

    @Override
    public Mono<ResponsePaymentDto> findPaymentById(int paymentId) {
        Mono<PaymentDto> paymentDtoMono = this.paymentControlService.findPaymentById(paymentId);
        return paymentDtoMono.flatMap(paymentDto -> {
            Flux<OrderDto> orderDtoFlux = orderControlService.findOrdersByPaymentId(paymentId);
            return orderDtoFlux.collectList()
                    .map(orderDtos -> new ResponsePaymentDto(paymentDto.getId(), paymentDto.getCurrency(),
                            paymentDto.getStatus(), paymentDto.getPaymentMethod(), paymentDto.getAmount(), orderDtos));
        });
    }

    @Override
    public Mono<ResponsePaymentDto> createPayment(RequestPaymentDto requestPaymentDto) {
        return Flux.fromIterable(requestPaymentDto.getOrdersId()).flatMap(orderId -> {
            return orderControlService.findOrderById(orderId)
                    // Check if order exists
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Order with " + "ID " + orderId + " not found")))
                    .flatMap(orderDto -> {
                        // Check if order doesn't already have a payment
                        if (orderDto.getPaymentId() != null) {
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "An order is " +
                                    "already in payment"));
                        }
                        return Mono.just(orderDto);
                    });
        }).collectList().flatMap(orderDtoList -> {
            // Everything is good, proceed to payment

            return paymentControlService.createPayment(requestPaymentDto, orderDtoList).flatMap(paymentDto -> {
                // Update each order with the right payment ID
                return Flux.fromIterable(requestPaymentDto.getOrdersId())
                        .flatMap(orderId -> orderControlService.updatePaymentId(orderId, paymentDto.getId()))
                        .collectList()
                        .map(updatedOrders -> new ResponsePaymentDto(paymentDto.getId(), paymentDto.getCurrency(),
                                paymentDto.getStatus(), paymentDto.getPaymentMethod(), paymentDto.getAmount(),
                                updatedOrders));
            });
        });
    }

    @Override
    public Mono<ResponsePaymentDto> updatePaymentStatus(RequestUpdatePaymentStatusDto requestUpdatePaymentStatusDto) {
        return paymentControlService.findPaymentById(requestUpdatePaymentStatusDto.getPaymentId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found")))
                .flatMap(paymentDto -> paymentControlService.updatePaymentStatus(requestUpdatePaymentStatusDto.getPaymentId(), requestUpdatePaymentStatusDto.getStatus())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "Error during " + "update."))))
                .flatMap(paymentDto -> {
                    // Retrieve orders
                    return orderControlService.findOrdersByPaymentId(paymentDto.getId()).collectList()
                            .map(orderDtos -> new ResponsePaymentDto(paymentDto.getId(), paymentDto.getCurrency(),
                                    paymentDto.getStatus(), paymentDto.getPaymentMethod(), paymentDto.getAmount(),
                                    orderDtos));

                });
    }

    @Override
    public Mono<ResponseOrderDto> findOrderById(int orderId) {
        Mono<OrderDto> orderDtoMono = this.orderControlService.findOrderById(orderId);
        return orderDtoMono.flatMap(orderDto -> {
            Mono<PaymentDto> paymentDtoMono = this.paymentControlService.findPaymentById(orderDto.getPaymentId());
            return paymentDtoMono.map(paymentDto -> new ResponseOrderDto(orderDto.getId(), orderDto.getProductName(),
                    orderDto.getProductReference(), orderDto.getQuantity(), orderDto.getPrice(), paymentDto));

        });
    }
}
