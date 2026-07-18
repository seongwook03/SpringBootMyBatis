package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrDTO {

    private String filePath;
    private String fileName;
    private String textFromImage;

    private String orgFileName;
    private String ext;
    private String regId;
    private String chgId;
}
