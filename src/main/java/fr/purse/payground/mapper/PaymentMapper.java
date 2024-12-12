package fr.purse.payground.mapper;

import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapstruct mapper for Payment
 */
@Mapper(uses = OrderMapper.class)
public interface PaymentMapper {

    /**
     * Unique instance of PaymentMapper
     */
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    /**
     * Convert Payment entity to PaymentDto
     *
     * @param payment the entity
     * @return the Dto
     */
    PaymentDto toDto(Payment payment);

    /**
     * Convert the PaymentDto to Payment entity
     *
     * @param paymentDto the dto
     * @return the entity
     */
    Payment fromDto(PaymentDto paymentDto);

    /**
     * Convert a RequestPaymentDto to a Payment entity
     *
     * @param requestPaymentDto the request
     * @return an entity
     */
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    Payment fromRequest(RequestPaymentDto requestPaymentDto);
}
