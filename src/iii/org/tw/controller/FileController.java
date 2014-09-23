package iii.org.tw.controller;

import iii.org.tw.model.FileTokenModel;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/fileUpload")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/fileUpload", description = "Smart Tourism FileUpload Api")
public class FileController {

	Logger logger = LoggerFactory.getLogger(FileController.class);

	public static Properties props = new Properties();

	static {
		try {
			props.load(FileController.class
					.getResourceAsStream("/db.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "單一檔案上傳，並取得檔案token",notes="單一檔案上傳，並取得檔案token" , response = FileTokenModel.class)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	public String fileUpload(@ApiParam(required=true,value="上傳檔案")@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) throws SmartTourismException {

		logger.info("-----fileUpload-----");

		String extension = fileDetail.getFileName().substring(fileDetail.getFileName().lastIndexOf("."));
		String fileName = UUID.randomUUID().toString() + extension;

		upload(uploadedInputStream, props.getProperty("file.upload.path") + fileName);

		Gson gson = new Gson();
		String json = gson.toJson(new FileTokenModel(fileName));

		return json;
	}

	private void upload(InputStream uploadedInputStream, String uploadedFileLocation) throws SmartTourismException {

		try (	
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
		) 
		{			

			int read = 0;
			byte[] bytes = new byte[1024];
 
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();

		} catch (IOException e) {

			e.printStackTrace();
			throw new SmartTourismException("檔案上傳發生例外");
		}

	}

}