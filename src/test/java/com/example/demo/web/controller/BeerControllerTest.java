package com.example.demo.web.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import com.example.demo.repositories.BeerRepository;
import com.example.demo.web.model.BeerDto;
import com.example.demo.web.model.BeerStyleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.example.demo.web.mappers")
class BeerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BeerRepository beerRepository;

	BeerDto getValidBeerDto() {
		return BeerDto.builder().beerName("My Beer").beerStyle(BeerStyleEnum.ALE).price(new BigDecimal("2.99"))
				.upc(123123123L).build();
	}

	@Test
	void testGetBeerById() throws Exception {
		mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
				.param("isCold","yes")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("v1/beer",
						pathParameters(
								parameterWithName("beerId").description("UUID of desired beer to get.")
								),
						requestParameters(
								parameterWithName("isCold").description("Is Beer cold query parameter.")
								),
						responseFields(
								fieldWithPath("id").description("Id of Beer."),
								fieldWithPath("version").description("Version number."),
								fieldWithPath("createdDate").description("Date created."),
								fieldWithPath("lastModifiedDate").description("Date updated."),
								fieldWithPath("beerName").description("Beer Name."),
								fieldWithPath("beerStyle").description("Beer Style."),
								fieldWithPath("upc").description("UPC of Beer."),
								fieldWithPath("price").description("Price."), 
								fieldWithPath("quantityOnHand").description("Quantity on Hand.")
								)
						));
	}

	@Test
	void testSaveNewBeer() throws Exception {
		BeerDto beerDto = getValidBeerDto();
		String beerDtoJson = objectMapper.writeValueAsString(beerDto);
		
		ConstrainedFields fields = new ConstrainedFields(BeerDto.class);
		mockMvc.perform(post("/api/v1/beer/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson))
				.andExpect(status().isCreated())
				.andDo(document("v1/beer",
						requestFields(
								fields.withPath("id").ignored(),
								fields.withPath("version").ignored(),
								fields.withPath("createdDate").ignored(),
								fields.withPath("lastModifiedDate").ignored(),
								fields.withPath("beerName").description("Beer Name."),
								fields.withPath("beerStyle").description("Beer Style."),
								fields.withPath("upc").description("UPC of Beer."),
								fields.withPath("price").description("Price."), 
								fields.withPath("quantityOnHand").ignored()
								)
						));
	}

	@Test
	void testUpdateBeerById() throws Exception {
		BeerDto beerDto = getValidBeerDto();
		String beerDtoJson = objectMapper.writeValueAsString(beerDto);

		mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString()).contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson)).andExpect(status().isNoContent());

	}
	
	private static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;
        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }
        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}
