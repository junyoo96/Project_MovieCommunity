package jun.moviecommunity.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties : 클래스 단위 레벨에 사용되며 지정된 필드값의 JSON 직렬화는 허용하지만, 역직렬화는 허용 안함
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SmartEditor {
    private String sFileURL;
    private String sFileName;
    private String bNewLine;
}
