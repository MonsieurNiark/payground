package fr.purse.payground.controller;


import fr.purse.payground.control.OrderControlService;
import fr.purse.payground.control.PaymentOrderControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.request.RequestOrderDto;
import fr.purse.payground.dto.response.ResponseOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    private final PaymentOrderControlService paymentOrderControlService;

    /**
     * Constructor
     *
     * @param orderControlServiceParam to control order
     */
    @Autowired
    public OrderController(OrderControlService orderControlServiceParam,
                           PaymentOrderControlService paymentOrderControlServiceParam) {
        this.orderControlService = orderControlServiceParam;
        this.paymentOrderControlService = paymentOrderControlServiceParam;
    }

    /**
     * Retrieve an order by its ID
     *
     * @param orderId the order id
     * @return a Mono of OrderDto
     */
    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseOrderDto> findOrderById(@PathVariable int orderId) {
        return paymentOrderControlService.findOrderById(orderId);
    }

    /**
     * Create an order
     *
     * @param requestOrderDto a request order
     * @return a Mono of OrderDto
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderDto> createOrder(@RequestBody RequestOrderDto requestOrderDto) {
        return this.orderControlService.createOrder(requestOrderDto);
    }
}
