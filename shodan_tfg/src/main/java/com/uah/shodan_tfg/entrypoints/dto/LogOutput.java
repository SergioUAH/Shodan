package com.uah.shodan_tfg.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogOutput {

    private String text;
    private String timeStamp;

}
