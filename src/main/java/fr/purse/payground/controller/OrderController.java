package fr.purse.payground.controller;


import fr.purse.payground.control.OrderControlService;
import fr.purse.payground.control.PaymentControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.request.RequestOrderDto;
import fr.purse.payground.dto.response.ResponseOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller of Order
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderControlService orderControlService;
    private final PaymentControlService paymentControlService;

    /**
     * Constructor
     *
     * @param orderControlServiceParam   to control order
     * @param paymentControlServiceParam to control payment
     */
    @Autowired
    public OrderController(OrderControlService orderControlServiceParam,
                           PaymentControlService paymentControlServiceParam) {
        this.orderControlService = orderControlServiceParam;
        this.paymentControlService = paymentControlServiceParam;
    }

    /**
     * Retrieve an order by its ID
     *
     * @param orderId the order id
     * @return a Mono of OrderDto
     */
    @GetMapping("/{orderId}")
    public Mono<ResponseOrderDto> findOrderById(@PathVariable int orderId) {
        Mono<OrderDto> orderDtoMono = this.orderControlService.findOrderById(orderId);
        return orderDtoMono.flatMap(orderDto -> {
            Mono<PaymentDto> paymentDtoMono = this.paymentControlService.findPaymentById(orderDto.getPaymentId());
            return paymentDtoMono.map(paymentDto -> new ResponseOrderDto(orderDto.getId(), orderDto.getProductName(),
                    orderDto.getProductReference(), orderDto.getQuantity(), orderDto.getPrice(), paymentDto));

        });
    }

    /**
     * Create an order
     *
     * @param requestOrderDto a request order
     * @return a Mono of OrderDto
     */
    @PostMapping
    public Mono<OrderDto> createOrder(@RequestBody RequestOrderDto requestOrderDto) {
        return this.orderControlService.createOrder(requestOrderDto);
    }
}
