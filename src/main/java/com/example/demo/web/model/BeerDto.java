package com.example.demo.web.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
	
	@Null
	UUID id;
	
	@Null
	Integer version;
	
	@Null
	OffsetDateTime createdDate;
	
	@Null
	OffsetDateTime lastModifiedDate;
	
	@NotBlank
	@Size(min = 3, max = 100)
    String beerName;
	
	@NotNull
    BeerStyleEnum beerStyle;
    
	@Positive
	@NotNull
	Long upc;
	
	@NotNull
	@Positive
    BigDecimal price; 
	
	@Positive
    Integer quantityOnHand;
}
