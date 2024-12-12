package fr.purse.payground.internal.control;

import fr.purse.payground.control.PaymentControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.PaymentStatusEnum;
import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.entity.Payment;
import fr.purse.payground.mapper.PaymentMapper;
import fr.purse.payground.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of {@link PaymentControlService}
 */
@Service
public class PaymentControlServiceImpl implements PaymentControlService {

    private static final PaymentMapper PAYMENT_MAPPER = PaymentMapper.INSTANCE;

    private final PaymentRepository paymentRepository;

    /**
     * Constructor
     *
     * @param paymentRepositoryParam payment repository
     */
    @Autowired
    public PaymentControlServiceImpl(PaymentRepository paymentRepositoryParam) {
        this.paymentRepository = paymentRepositoryParam;
    }

    @Override
    public Mono<PaymentDto> findPaymentById(final int id) {
        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found")))
                .map(PAYMENT_MAPPER::toDto);
    }

    @Override
    public Mono<PaymentDto> createPayment(final RequestPaymentDto requestPaymentDto,
                                          final List<OrderDto> orderDtoList) {
        Payment payment = PAYMENT_MAPPER.fromRequest(requestPaymentDto);
        BigDecimal amount = orderDtoList.stream()
                .map(orderDto -> BigDecimal.valueOf(orderDto.getQuantity()).multiply(orderDto.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatusEnum.IN_PROGRESS);
        return paymentRepository.save(payment).map(PAYMENT_MAPPER::toDto);
    }

    @Override
    public Mono<PaymentDto> updatePaymentStatus(final int id, final PaymentStatusEnum status) {
        return paymentRepository.findById(id).filter(payment -> validateStatus(payment.getStatus(), status))

                .flatMap(payment -> {
                    payment.setStatus(status);
                    return paymentRepository.save(payment);
                }).map(PAYMENT_MAPPER::toDto);
    }

    private boolean validateStatus(PaymentStatusEnum oldStatus, PaymentStatusEnum newStatus) {
        if (oldStatus.equals(newStatus)) {
            return true;
        }
        if ((PaymentStatusEnum.AUTHORIZED.equals(newStatus) && (PaymentStatusEnum.IN_PROGRESS.equals(oldStatus))) || PaymentStatusEnum.AUTHORIZED.equals(oldStatus) && (PaymentStatusEnum.IN_PROGRESS.equals(newStatus))) {
            return true;
        }
        if (PaymentStatusEnum.CAPTURED.equals(newStatus) && PaymentStatusEnum.AUTHORIZED.equals(oldStatus)) {
            return true;
        }
        return false;
    }
}
