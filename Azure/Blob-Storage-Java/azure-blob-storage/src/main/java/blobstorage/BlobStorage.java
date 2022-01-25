package blobstorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class BlobStorage {
	public static void main(String[] args) throws IOException {
		String storageConnectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
		try {
			// Referencia a la cadena de conexión
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Crear el cliente del blob
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Referencia al contenedor y lo crea si no existe
			CloudBlobContainer container = blobClient.getContainerReference("nuevo");
			container.createIfNotExists();
			
						
			//Subir un blob a Azure Blob Storage
				
			String csv = "Alumnos.csv";
			CloudBlockBlob blob1 = container.getBlockBlobReference(csv);
			File source = new File(csv);
			blob1.upload(new FileInputStream(source), source.length());
			

			
			//Obtienen los Blobs del contenedor y los descarga en una carpeta		
			   for (ListBlobItem blobItem : container.listBlobs()) {
				   // Verifica si el objeto es en realidad un BLOB
				   if (blobItem instanceof CloudBlob) {
					   //Descarga el archivo y lo guarda con el mismo nombre
				        CloudBlob blob = (CloudBlob) blobItem;
				        blob.download(new FileOutputStream("./data/" + blob.getName()));
				}
			   }
	} catch (Exception e) {
			e.printStackTrace();
	}
		
}
}