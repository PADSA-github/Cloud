package download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.microsoft.azure.storage.blob.*;
import com.azure.storage.blob.BlobClient;
import com.microsoft.azure.storage.*;

public class Download {
	public static void main(String[] args) throws IOException {
		String storageConnectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
		try {
			// Referencia a la cadena de conexión
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Crear el cliente del blob
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Referencia al contenedor y lo crea si no existe
			CloudBlobContainer container = blobClient.getContainerReference("imagenes2");
			container.createIfNotExists();

					
			   for (ListBlobItem blobItem : container.listBlobs()) {
			       // If the item is a blob, not a virtual directory.
			       if (blobItem instanceof CloudBlob) {
			           // Download the item and save it to a file with the same name.
				        CloudBlob blob = (CloudBlob) blobItem;
				        blob.download(new FileOutputStream("./data/" + blob.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
		
	}
}