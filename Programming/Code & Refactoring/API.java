package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
/**
 * Class cung cấp các phương thức giúp gửi request và nhận dữ liệu trả về
 * Date: 12/12/2021
 * @author Viet
 * @version 1.0
 *
 */
public class API {

	/**
	 * thuộc tính giúp format ngày tháng theo định dạng
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * thuộc tính giúp log thông tin ra console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());
	
	/**
	 * Thiết lập connection tới server
	 * @param url: đường dẫn tới server
	 * @param method: phương thức
	 * @param token: mã xác thực người dùng
	 * @return connection
	 * @throws IOException
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token) throws IOException {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		
		return conn;
	}
	
	/**
	 * phương thức đọc dữ liệu trả về từ server
	 * @param conn: connection tới server
	 * @return response: phản hồi trả về từ server
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Respone Info: " + response.toString());
		return response.toString();
	}

	/**
	 * phương thức giúp gọi API GET
	 * @param url: đường dẫn tới server
	 * @param token: Mã xác thực người dùng
	 * @return response: phản hồi trả về từ server
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		
		// phần 1: setup
		HttpURLConnection conn = setupConnection(url, "GET", token);
		
		// phần 2: đọc dữ liệu trả về
		String response = readResponse(conn);
		
		return response;
	}

	int var;

	/**
	 * phương thức gọi API POST
	 * @param url: đường dẫn tới server 
	 * @param data: dữ liệu đưa lên server (JSON)
	 * @return response: phản hồi trả về từ server (string)
	 * @throws IOException
	 */
	public static String post(String url, String data
			// , String token
	) throws IOException {
		allowMethods("PATCH");
		
		// phần 1: setup
		HttpURLConnection conn = setupConnection(url, "POST", null);

		// phan 2: gửi dữ liệu
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();
		
		// phan 3: đọc dữ liệu trả về
		String response = readResponse(conn);
		
		return response;
	}

	/**
	 * Phương thức gọi API khác như PATCH, PUT,..
	 * @deprecated chỉ hoạt động với Java <= 11
	 * @param methods: giao thức cần cho phép (PATCH, PUT,..)
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
