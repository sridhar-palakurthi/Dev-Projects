package util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntityToJsonBuilder {

	public static String convertToJson(Object object) {
		String json = "";
		
		try {
			com.fasterxml.jackson.databind.ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(object);

		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
}
