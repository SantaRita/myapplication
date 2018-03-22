package com.meditacionback.utiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ChangeEvent;
import com.vaadin.ui.Upload.ChangeListener;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class UploadBox extends CustomComponent implements Receiver, ChangeListener, ProgressListener, FailedListener, SucceededListener, StartedListener {
	// Put upload in this memory buffer that grows automatically
	ByteArrayOutputStream os = new ByteArrayOutputStream(10240);
	public static final String storageConnectionString =
	"DefaultEndpointsProtocol=https;AccountName=multimediablob;AccountKey=VLUsHCFjTEux65BxBgKOV0kLBpHe9qkPiv7HvbUaTXestWjheAKsSnL6VW00XjrTN/P+YcEjVotwTW06jiCkMQ==;EndpointSuffix=core.windows.net";


	// Name of the uploaded file
	String filename;

	ProgressBar progress = new ProgressBar(0.0f);
	
	public String nombreFichero;
	public String nombreFicheroSeleccionado;
	public Upload upload;

	// Show uploaded file in this placeholder
	Image image = new Image("Uploaded Image");

	public UploadBox() {
		// Create the upload component and handle all its events
		upload = new Upload("File", null);
		upload.setReceiver(this);
		upload.addProgressListener(this);
		upload.addFailedListener(this);
		upload.addSucceededListener(this);
		upload.addStartedListener(this);
		upload.addChangeListener(this);
		upload.setButtonCaption(null);

		// Put the upload and image display in a panel
		VerticalLayout vl = new VerticalLayout();
		//panel.setWidth("100%");
		//VerticalLayout panelContent = new VerticalLayout();
		//panelContent.setSpacing(true);
		//panel.setContent(panelContent);
		//panelContent.addComponent(upload);
		//panelContent.addComponent(progress);
		//panelContent.addComponent(image);

		progress.setVisible(true);
		progress.setWidth("100%");
		image.setVisible(false);

	
		vl.addComponent(upload);
		vl.addComponent(new Label(""));
		vl.addComponent(progress);
		
		setCompositionRoot(vl);
	}
	


	@Override
	public void filenameChanged(ChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Seleccionamos el fichero fichero");
		nombreFicheroSeleccionado = event.getFilename();
	}



	public OutputStream receiveUpload(String filename, String mimeType) {
		System.out.println("receiveupload");
		this.filename = filename;
		os.reset(); // Needed to allow re-uploading
		return os;
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		System.out.println("progress");
		progress.setVisible(true);
		if (contentLength == -1)
			progress.setIndeterminate(true);
		else {
			progress.setIndeterminate(false);
			progress.setValue(((float) readBytes) / ((float) contentLength));
		}
	}
	


	@Override
	public void uploadStarted(StartedEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos a started");
		//nombreFichero = nombreFicheroSeleccionado;
		
	}

	public void uploadSucceeded(SucceededEvent event) {
		System.out.println("uploadsuces");
		image.setVisible(true);
		image.setCaption("Uploaded Image " + filename + " has length " + os.toByteArray().length);

		// Display the image as a stream resource from
		// the memory buffer
		StreamSource source = new StreamSource() {
			public InputStream getStream() {
				return new ByteArrayInputStream(os.toByteArray());
			}
		};

		/*if (image.getSource() == null)
			// Create a new stream resource
			image.setSource(new StreamResource(source, filename));
		else { // Reuse the old resource
			StreamResource resource = (StreamResource) image.getSource();
			resource.setStreamSource(source);
			resource.setFilename(filename);
		}

		image.markAsDirty();*/
		
		// Subimos el fichero a la nube de Azure
		try {
			CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("photos");
            container.createIfNotExists();

            // Upload an image file.
            System.out.println("Fichero Azure: " + nombreFichero);
            CloudBlockBlob blob = container.getBlockBlobReference(nombreFichero);
            //
            //File sourceFile = new File(ruta);
            blob.uploadFromByteArray(os.toByteArray(), 0, os.toByteArray().length);
            //blob.upload(new java.io.FileInputStream(sourceFile), sourceFile.length());

            // Download the image file.
            //File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
            //blob.downloadToFile(destinationFile.getAbsolutePath());
            
            System.out.println("FIchero subido correctamente");
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.out.print("FileNotFoundException encountered: ");
            System.out.println(fileNotFoundException.getMessage());
        }
        catch (StorageException storageException) {
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
        }
        catch (Exception e) {
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
        }	
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		Notification.show("Upload failed", Notification.Type.ERROR_MESSAGE);
	}
}