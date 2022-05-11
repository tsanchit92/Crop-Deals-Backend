package farm.razor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
	private String userName;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String amount;
}
