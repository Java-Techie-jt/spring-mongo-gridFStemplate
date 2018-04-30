package com.javatechie.spring.mongo.binary.api.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@RestController
public class BinaryDataController {

	@Autowired
	private GridFsOperations gridFsOperations;

	String fileId = "";

	@GetMapping("/saveFiles")
	public String saveFile() throws FileNotFoundException {
		// define metadata
		DBObject metaData = new BasicDBObject();
		metaData.put("organization", "Java Techie");

		// store image file
		InputStream inputStream = new FileInputStream("C:/Users/bahota/Desktop/logo.png");
		metaData.put("type", "image");

		fileId = gridFsOperations.store(inputStream, "logo.png", "image/png", metaData).getId().toString();
		System.out.println("File id stored : " + fileId);

		// store text file
		metaData.put("type", "data");
		gridFsOperations.store(new FileInputStream("C:/Users/bahota/Desktop/service-req&resp.txt"), "myText.txt",
				"text/plain", metaData);

		return "File stored successfully...";
	}

	@GetMapping("/retrive/image")
	public String retriveImageFile() throws IOException {
		GridFSDBFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
		dbFile.writeTo("C:/Users/bahota/Desktop/Local-Disk/reactive-logo.png");
		System.out.println("File name : " + dbFile.getFilename());
		return "Image File retrived with name : " + dbFile.getFilename();
	}

	@GetMapping("/retrive/text")
	public String retriveTextFile() throws IOException {
		GridFSDBFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("metadata.type").is("data")));
		dbFile.writeTo("C:/Users/bahota/Desktop/Local-Disk/myData.txt");
		System.out.println("File name : " + dbFile.getFilename());
		return "Text File retrived with name : " + dbFile.getFilename();
	}

}
