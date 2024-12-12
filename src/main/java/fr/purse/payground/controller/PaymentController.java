package fr.purse.payground.controller;


import fr.purse.payground.control.OrderControlService;
import fr.purse.payground.control.PaymentControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.dto.request.RequestUpdatePaymentStatusDto;
import fr.purse.payground.dto.response.ResponsePaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller to create update and retrieve Payment informations
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentControlService paymentControlService;
    private final OrderControlService orderControlService;

    /**
     * Constructor
     *
     * @param paymentControlServiceParam payment control service
     * @param orderControlServiceParam   order control service
     */
    @Autowired
    public PaymentController(PaymentControlService paymentControlServiceParam,
                             OrderControlService orderControlServiceParam) {
        this.paymentControlService = paymentControlServiceParam;
        this.orderControlService = orderControlServiceParam;
    }

    /**
     * Find a payment by its id. Throw 404 if not found
     *
     * @param paymentId the payment id
     * @return a Mono of {@link ResponsePaymentDto}
     */
    @GetMapping("/{paymentId}")
    public Mono<ResponsePaymentDto> findPaymentById(@PathVariable int paymentId) {
        Mono<PaymentDto> paymentDtoMono = this.paymentControlService.findPaymentById(paymentId);
        return paymentDtoMono.flatMap(paymentDto -> {
            Flux<OrderDto> orderDtoFlux = orderControlService.findOrdersByPaymentId(paymentId);
            return orderDtoFlux.collectList()
                    .map(orderDtos -> new ResponsePaymentDto(paymentDto.getId(), paymentDto.getCurrency(),
                            paymentDto.getStatus(), paymentDto.getPaymentMethod(), paymentDto.getAmount(), orderDtos));
        });

    }

    /**
     * Doing a post create a payment binded to the given orders IDs
     * Throws 404 if one order is not found, throw a 403 is the order has already a payment
     *
     * @param requestPaymentDto the body of the request
     * @return a {@link ResponsePaymentDto}
     */
    @PostMapping
    public Mono<ResponsePaymentDto> createPayment(@RequestBody RequestPaymentDto requestPaymentDto) {
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


    /**
     * Update the payment status with a PUT request
     *
     * @param requestUpdatePaymentStatusDto the payment payload
     * @return a Mono of {@link ResponsePaymentDto}
     */
    @PutMapping
    public Mono<ResponsePaymentDto> updatePaymentStatus(@RequestBody RequestUpdatePaymentStatusDto requestUpdatePaymentStatusDto) {
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
}
