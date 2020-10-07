package com.uah.shodan_tfg.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HostDTO {

    private Integer id;

    private String ip;
    private Integer port;

    private String hostname;
    private String os;

    private LocationDTO location;

}
