package farm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import farm.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	
	@Query(value = "select * from cart_item c where c.user_name=:dealer",nativeQuery = true)
	List<CartItem> getCartItem(@Param("dealer") String dealer);
	

	@Query(value ="select * from cart_item c where c.user_name= null",nativeQuery = true)
	void deleteDealerCart();
	
}
