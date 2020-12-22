package br.com.app.vinygram.aspect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloundinaryAsync {

	private MultipartFile multipartFile;
	private String name;

	protected void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	@Async
	protected synchronized Thread uploadImage(String folder) {

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					
					enviar(folder);
				} catch (IOException e) {

				}
			}

		};

		return thread;
	}

	private void enviar(String folder) throws IOException {
		
		System.out.println(folder);
		System.out.println(name);
		
		File file = convertMultiPartToFile(this.multipartFile);
		Cloudinary cloudinary = new Cloudinary();
		cloudinary.uploader().upload(file,
				ObjectUtils.asMap("cloud_name", "devblack", "api_key", "915778129293287", "api_secret",
						"f9lUAyR4cmNu8yr22vN6nG4hZSg", "resource_type", "image", "public_id", name, "version", "1",
						"async", true, "invalidate", true, "unique_filename", true, "format", "jpg", "type", "upload",
						"folder", folder));
	}

	@SuppressWarnings("unused")
	private void rename() {

	}
	
	@Async
	protected synchronized Thread removeAsync(String folder,String name) {
		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					remove(folder, name);

				} catch (Exception e) {

				}
			}
		};

		return thread;
	}

	private void remove(String folder,String name) throws Exception {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(folder).append("/").append(name);
		Cloudinary cloudinary = new Cloudinary();
		cloudinary.uploader().destroy(sBuffer.toString(), ObjectUtils.asMap("cloud_name", "devblack", 
				"api_key", "915778129293287", 
				"api_secret","f9lUAyR4cmNu8yr22vN6nG4hZSg",
				"invalidate", true, 
				"async",true,
				"resource_type" ,"image" ));
		notify();
	}

	@SuppressWarnings("unused")
	private void update() {}

	@SuppressWarnings("unused")
	private void returnImages() {}

	private synchronized void createfolder(String folder) throws Exception {
	
		Cloudinary cloudinary = new Cloudinary();
		cloudinary.api().createFolder(folder, ObjectUtils.asMap("cloud_name", "devblack", "api_key",
				"915778129293287", "api_secret", "f9lUAyR4cmNu8yr22vN6nG4hZSg"));
		notify();
	}
	
	@Async
	protected synchronized Thread createFolderAsync(String folder) {

		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					createfolder(folder);
					
				} catch (Exception e) {

				}
			}
		};
	
		return thread;
	}

}