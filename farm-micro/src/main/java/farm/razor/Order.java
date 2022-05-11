package farm.razor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_order")
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Order implements Serializable {
 
    /**
     * 
     */
    private static final long serialVersionUID = 65981149772133526L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    
    private String userId;
    
    private String razorpayPaymentId;
 
    private String razorpayOrderId;
 
    private String razorpaySignature;
 
 
}

