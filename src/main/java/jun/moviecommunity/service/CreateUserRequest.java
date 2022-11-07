package jun.moviecommunity.service;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    private String name;
    private String password;
    private String nickname;
    private String email;
}
