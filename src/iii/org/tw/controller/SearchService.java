package iii.org.tw.controller;

import iii.org.tw.entity.Activity;
import iii.org.tw.model.SuggestionResultModel;
import iii.org.tw.service.CompleteWords;
import iii.org.tw.service.InputWords;
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

 
@Path("/Search")
@Api(value = "/Search", description = "Operations about Search")
public class SearchService {
	
	
	@GET
	public String guide() {
	   return "Please input keywords and set header \"lang\", lang value (0:Chinese,1:English,2:Japanese);";
	}   
	
	@GET
    @Path("/{content}")
	@ApiOperation(value = "Search",notes="輸入關鍵字並選擇語系" , response = String.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

	 public String analysis(@ApiParam(required=true,value="Input Keywords")@PathParam("content") String content,@ApiParam(required=false,value="zh_tw, en_us, ja_jp",
     allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language		) throws Exception
    { 
		int lang = 0;
		if (language.equals("ja_jp")){
			lang = 2;
			}else if (language.equals("en_us")){
			lang = 1;

			}else{
				lang = 0;
			}
		System.out.println(lang);

		String results = InputWords.main(content,lang);
		return results;

	//	String results = Classification.
    }
	
	
}