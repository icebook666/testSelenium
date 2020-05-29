package runSelenium;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;

public class ImageBase64Utils {
	public static void main(String[] argv) throws Exception {
		//String str = imageToBase64String("D:/testimg/test1.jpg");
		//System.out.println(str);
		//saveBase64ImageStringToImage("D:/testimg/", "t01", str);
		
		String FILE_URL = "https://www.humanesociety.org/sites/default/files/styles/400x400/public/2018/06/cat-217679.jpg?h=c4ed616d&amp;itok=H0FcH69a";
		saveURLImage("D:/JAR", "a1", FILE_URL);
		
	}

	/**
	 * 把圖片轉換為Base64編碼的字符串
	 *
	 * @param imagePath 圖片的路徑
	 * @return 回傳圖片的Base64編碼的字串
	 * @throws IOException
	 */
	public static String imageToBase64String(String imagePath) throws IOException {
		// 圖片的格式為data:image/jpeg;base64,/9j/4AAQSkZJRgAB...
		// 逗號的前面為圖片的格式，逗號後為圖片二進制Data的Base64編碼字串
		String prefix = String.format("data:image/%s;base64,", FilenameUtils.getExtension(imagePath).toLowerCase());
		byte[] content = FileUtils.readFileToByteArray(new File(imagePath));
		return prefix + Base64.encodeBase64String(content);
	}

	/**
	 * 將Base64編碼字串表示的圖片保存到傳入的路徑directory下，圖片名稱為baseName(不含附檔名)
	 * 從base64ImageString中解析附檔名
	 *
	 * @param directory 保存圖片的路徑
	 * @param baseName 圖片名稱
	 * @param base64ImageString 圖片的Base64編碼字串
	 * @throws IOException
	 */
	public static void saveBase64ImageStringToImage(String directory, String baseName, String base64ImageString)
			throws IOException {
		// 圖片的格式為data:image/jpeg;base64,/9j/4AAQSkZJRgAB...
		// 逗號的前面為圖片的格式，逗號後為圖片二進制Data的Base64編碼字串
		int commaIndex = base64ImageString.indexOf(",");
		String extension = base64ImageString.substring(0, commaIndex).replaceAll("data:image/(.+);base64", "$1");
		byte[] content = Base64.decodeBase64(base64ImageString.substring(commaIndex));

		FileUtils.writeByteArrayToFile(new File(directory, baseName + "." + extension), content);
	}
	
	/**
	 * 保存urlPath的圖片到路徑directory下，圖片名稱為baseName(.jpg)
	 * @param directory 保存圖片的路徑
	 * @param baseName 圖片名稱
	 * @param urlPath 圖片所在網址
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void saveURLImage(String directory, String baseName, String urlPath) throws MalformedURLException, IOException
	{
		String file_extension = "jpg";
		BufferedInputStream in = new BufferedInputStream(new URL(urlPath).openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, baseName + "." + file_extension));

		byte dataBuffer[] = new byte[1024];
		int bytesRead;
		while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
			fileOutputStream.write(dataBuffer, 0, bytesRead);
		}
		
		fileOutputStream.close();
	}
}