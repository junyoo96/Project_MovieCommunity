package jun.moviecommunity.service;

import jun.moviecommunity.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String nickname;
    private String imagePath;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.imagePath = user.getImagePath();
    }
}
