package com.rupeek.APIproject.scripts;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
/**
 * 
 * @author Amit
 *
 */
public class CustomerOperation 
{

	String beartoken;
	String phoneno;
	
	 /**
	  * precondition
	  */
	 @BeforeMethod()
	 public void preCondition()
	 {
	   baseURI="http://13.126.80.194:8080";	 
	 }
	 
	 
	/**
	* to get beartoken
	*/	 
	@Test(priority=1)
	public void getBearToken()
	{
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("username", "rupeek");
		jsonObject.put("password","password");
		Response response = given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().
		post("/authenticate");
		beartoken=response.jsonPath().getString("token");
		response.then().assertThat().contentType(ContentType.JSON).and().statusCode(201);
		 
	}
	
	/**
	 * get beartoken without username
	 */
	@Test(priority=2)
	public void getBearTokenWithoutUsername()
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("password","password");
		Response response = given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().
		post("/authenticate");
		String beartoken2=response.jsonPath().getString("token");
		response.then().assertThat().body(beartoken2,Matchers.equalTo("token"));
		
	}
	
	/**
	 * get beartoken without password
	 */
	@Test(priority=3)
	public void getBearTokenWithoutPassword()
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("username", "rupeek");
		Response response = given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().
		post("/authenticate");
		String beartoken3=response.jsonPath().getString("token");
		response.then().assertThat().body(beartoken3,Matchers.equalTo("token"));
		
	}
	
	/**
	 * get beartoken without username and password
	 */
	@Test(priority=4)
	public void getBearTokenWithoutUsernameAndPassword()
	{
		JSONObject jsonObject=new JSONObject();
		Response response = given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().
		post("/authenticate");
		String beartoken3=response.jsonPath().getString("token");
		response.then().assertThat().body(beartoken3,Matchers.equalTo("token"));
	}
	
	
	/**
	 * to get all customers details
	 */
	
	@Test(priority=5)
	public void getAllCustomerRecord()
	{
		Response response = given().auth().oauth2(beartoken)
	    .when().get("api/v1/users");
		response.prettyPeek().then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
	    phoneno= response.jsonPath().getString("phone[0]");
	}
	
	
	
	
	/**
	 * to get customer by phone no
	 */
	@Test(priority=6)
	public void getCustomerByPhoneNo()
	{
		given().auth().oauth2(beartoken).pathParam("phoneNo",phoneno)
	    .when().get("/api/v1/users/{phoneNo}").prettyPeek().then().
	    assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}
	
	
	/**
	 *to get customer with wrong phoneno 
	 */
	@Test(priority=7)
	public void getCustomerByWrongPhoneNo()
	{
		String phoneNumber="1122334455";
		given().auth().oauth2(beartoken).pathParam("phoneNo",phoneNumber)
	    .when().get("/api/v1/users/{phoneNo}").prettyPeek().then().
	    assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	
	/**
	 *to get customer with blank phoneno 
	 */
	@Test(priority=8)
	public void getCustomerWithBlankNo()
	{
		String phoneNumber="";
		given().auth().oauth2(beartoken).pathParam("phoneNo",phoneNumber)
	    .when().get("/api/v1/users/{phoneNo}").prettyPeek().then().
	    assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	/**
	 *to get customer with eleven digit phoneno 
	 */
	@Test(priority=9)
	public void getCustomerWithElevenDigitNo()
	{
		String phoneNumber="11223344556";
		given().auth().oauth2(beartoken).pathParam("phoneNo",phoneNumber)
	    .when().get("/api/v1/users/{phoneNo}").prettyPeek().then().
	    assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}


}
