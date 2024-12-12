package fr.purse.payground.repository;

import fr.purse.payground.entity.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Reactive Payment CRUD Repository
 */
@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Integer> {
}
