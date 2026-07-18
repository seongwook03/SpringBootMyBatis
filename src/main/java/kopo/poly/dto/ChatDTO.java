package kopo.poly.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.SplittableRandom;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ChatDTO {

    private String name;

    private String msg;

    private String date;
}
