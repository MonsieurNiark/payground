package fr.purse.payground.repository;

import fr.purse.payground.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Reactive Order CRUD repository
 */
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Integer> {

    /**
     * Find orders by paymentId
     *
     * @param paymentId payment id
     * @return a Flux of orders
     */
    Flux<Order> findByPaymentId(int paymentId);
}
