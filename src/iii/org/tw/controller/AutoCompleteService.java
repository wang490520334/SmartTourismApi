package iii.org.tw.controller;

import iii.org.tw.service.CompleteWords;
import iii.org.tw.util.SmartTourismErrorMsg;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

 
@Path("/AutoComplete")
@Api(value = "/AutoComplete", description = "Operations about Keyword AutoComplete")
public class AutoCompleteService {
	
	
	@GET
	public String guide() {
	   return "Please input content";
	}   
	
	@GET
    @Path("/{content}")
	@ApiOperation(value = "Autocomplete",notes="依據使用者輸入的關鍵字，自動推薦完成整個字詞" , response = String.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

    public String analysis(@ApiParam(required=true,value="Input Keywords")@PathParam("content") String content,@ApiParam(required=false,value="zh_tw, en_us, ja_jp",
     allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language		) throws Exception
    { 
		int lang = 0;
		if (language == null) { //Swagger UI為選擇會出現Null Point Exception
			lang = 0;
		} else if (language.equals("ja_jp")) {
			lang = 2;
		} else if (language.equals("en_us")) {
			lang = 1;
		} else {
			lang = 0;
		}
		System.out.println(lang);

		String results = CompleteWords.main(content,lang);
		return results;

	//	String results = Classification.
    }

	
	
}