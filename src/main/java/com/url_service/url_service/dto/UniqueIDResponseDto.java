package com.url_service.url_service.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueIDResponseDto {
	private int randomID;
	private String status;
}
