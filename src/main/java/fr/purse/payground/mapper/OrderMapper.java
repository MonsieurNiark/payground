package fr.purse.payground.mapper;

import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.request.RequestOrderDto;
import fr.purse.payground.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapstruct mapper for Order
 */
@Mapper
public interface OrderMapper {

    /**
     * Unique instance of OrderMapper
     */
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Convert Order entity to OrderDto
     *
     * @param order the entity
     * @return the Dto
     */
    OrderDto toDto(Order order);

    /**
     * Convert the OrderDto to Order entity
     *
     * @param orderDto the dto
     * @return the entity
     */
    Order fromDto(OrderDto orderDto);

    /**
     * Convert a RequestOrderDto to an Order entity
     *
     * @param requestOrderDto a RequestOrderDto
     * @return the entity
     */
    Order fromDto(RequestOrderDto requestOrderDto);

}
