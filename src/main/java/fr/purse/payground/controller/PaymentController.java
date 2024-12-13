package fr.purse.payground.controller;


import fr.purse.payground.control.PaymentOrderControlService;
import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.dto.request.RequestUpdatePaymentStatusDto;
import fr.purse.payground.dto.response.ResponsePaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller to create update and retrieve Payment informations
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentOrderControlService paymentOrderControlService;

    /**
     * Constructor
     *
     * @param paymentOrderControlServiceParam Payment order control service
     */
    @Autowired
    public PaymentController(PaymentOrderControlService paymentOrderControlServiceParam) {
        this.paymentOrderControlService = paymentOrderControlServiceParam;
    }

    /**
     * Find a payment by its id. Throw 404 if not found
     *
     * @param paymentId the payment id
     * @return a Mono of {@link ResponsePaymentDto}
     */
    @GetMapping(path = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponsePaymentDto> findPaymentById(@PathVariable int paymentId) {
        return paymentOrderControlService.findPaymentById(paymentId);
    }

    /**
     * Doing a post create a payment binded to the given orders IDs
     * Throws 404 if one order is not found, throw a 403 is the order has already a payment
     *
     * @param requestPaymentDto the body of the request
     * @return a {@link ResponsePaymentDto}
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponsePaymentDto> createPayment(@RequestBody RequestPaymentDto requestPaymentDto) {
        return paymentOrderControlService.createPayment(requestPaymentDto);
    }


    /**
     * Update the payment status with a PUT request
     *
     * @param requestUpdatePaymentStatusDto the payment payload
     * @return a Mono of {@link ResponsePaymentDto}
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponsePaymentDto> updatePaymentStatus(@RequestBody RequestUpdatePaymentStatusDto requestUpdatePaymentStatusDto) {
        return paymentOrderControlService.updatePaymentStatus(requestUpdatePaymentStatusDto);
    }
}
