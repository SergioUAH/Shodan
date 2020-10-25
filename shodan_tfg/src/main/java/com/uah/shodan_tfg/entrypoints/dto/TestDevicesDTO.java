package com.uah.shodan_tfg.entrypoints.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestDevicesDTO {

	private List<Integer> ids;
	private List<String> wordlists;

}
