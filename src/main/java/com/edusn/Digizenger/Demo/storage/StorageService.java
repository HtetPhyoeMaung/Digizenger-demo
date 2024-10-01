package com.edusn.Digizenger.Demo.storage;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;
import java.net.URL;



import org.springframework.web.multipart.MultipartFile;



@Service
public class StorageService {
    private final AmazonS3 space;

    private static final String BUCKET_NAME = "digizenger-image";

    public StorageService() {
        // Create the AWS credentials provider
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("DO00ZTJLH6TMJEN8WEEL", "75BIqBS2Kgl7AWA3VUar993dgQbzr9fycrxQ/dEyEso")
        );

        // Initialize the Amazon S3 client
        space = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1")
                )
                .build();
    }



    public String  uploadImage(MultipartFile file) throws IOException {
        String filename = UUIDUtil.generateUUID()+file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        space.putObject(new PutObjectRequest(BUCKET_NAME, filename, file.getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    return filename;
    }

    public URL getImageByName(String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, imageName);

        return space.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public String  updateImage(MultipartFile file, String filename) throws IOException {

        // Check if the file exists in the bucket
        try {
            // Try to retrieve the existing object
            S3Object existS3Object = space.getObject("digizenger-image", filename);

            // If object exists, delete it
            if (existS3Object != null) {
                space.deleteObject(BUCKET_NAME, filename);
            }
        } catch (AmazonS3Exception e) {
            // Handle the case where the file does not exist
            if (e.getStatusCode() == 404) {
                System.out.println("File does not exist, proceeding with upload.");
            } else {
                throw new IOException("Failed to check existence of the file in S3: " + e.getMessage());
            }
        }
        String imageName = UUIDUtil.generateUUID()+file.getOriginalFilename();
        // Upload the new file with the same name
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        space.putObject(new PutObjectRequest(BUCKET_NAME, imageName, file.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return imageName;

    }

    public void deleteImage(String imageName) {
        // Deletes the specified image from the "digizenger-image" bucket
        space.deleteObject(BUCKET_NAME, imageName);
    }
}

