package iii.org.tw.model.JsonSerializer;

import java.lang.reflect.Type;

import iii.org.tw.model.PoiCalendarIOModel;
import iii.org.tw.util.SmartTourismException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PoiCalendarIOModelJsonSerializer implements
		JsonSerializer<PoiCalendarIOModel> {

	@Override
	public JsonElement serialize(PoiCalendarIOModel model, Type type,
			JsonSerializationContext context) {
		// TODO Auto-generated method stub
		
		Gson gson = new Gson();
		JsonElement element = gson.toJsonTree(model);
		JsonObject object = new JsonObject();
		try {
			object = element.getAsJsonObject();
			object.addProperty("type", model.getType() );
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartTourismException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object;
	}

}
