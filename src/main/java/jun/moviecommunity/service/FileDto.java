package jun.moviecommunity.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
    String fileName;
    String fileType;
    String fileSize;
}
