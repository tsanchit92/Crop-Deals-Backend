package farm.razor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


 
@Repository
public interface RazorOrderRepository extends JpaRepository<Order, Long> {
 
    Order findByRazorpayOrderId(String orderId);
}
