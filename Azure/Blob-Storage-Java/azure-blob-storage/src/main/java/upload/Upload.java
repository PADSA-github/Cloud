package upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class Upload {
	
	public static void main(String[] args) throws IOException {
			String storageConnectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
			try {
				// Referencia a la cadena de conexión
				CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

				// Crear el cliente del blob
				CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

				// Referencia al contenedor y lo crea si no existe
				CloudBlobContainer container = blobClient.getContainerReference("imagenes");
				container.createIfNotExists();

				
				String tree = "tree.jpg";
				CloudBlockBlob blob1 = container.getBlockBlobReference(tree);
				File source = new File(tree);
				blob1.upload(new FileInputStream(source), source.length());

				String dog = "dog.jpg";
				CloudBlockBlob blob2 = container.getBlockBlobReference(dog);
				source = new File(dog);
				blob2.upload(new FileInputStream(source), source.length());

			} catch (Exception e) {
				e.printStackTrace();
		}
		}
	}

