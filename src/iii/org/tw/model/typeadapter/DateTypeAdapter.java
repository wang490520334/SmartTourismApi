package iii.org.tw.model.typeadapter;

import java.io.IOException;
import java.sql.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {

	@Override
	public Date read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("read");
		
		return null;
	}

	@Override
	public void write(JsonWriter jsonWriter, Date arg1) throws IOException {
		//System.out.println("write");
		
		//jsonWriter.beginObject();
		jsonWriter.value(String.valueOf(arg1.getTime()));
		//jsonWriter.name("isbn").value(String.valueOf(arg1.getTime()));
		//jsonWriter.endObject();
		
		
	}

}
