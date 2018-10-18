package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.apigateway.model.Op;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {

    AmazonS3Client amazonS3Client;
    String photoStorageBucket;
    private Tika tika = new Tika();

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.amazonS3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.contentType);
        objectMetadata.setContentLength(blob.inputStream.available());
        amazonS3Client.putObject(photoStorageBucket,blob.name, blob.inputStream, objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        if (!amazonS3Client.doesObjectExist(photoStorageBucket, name)) {
            return Optional.empty();
        }

        try (S3Object s3Object = amazonS3Client.getObject(photoStorageBucket, name)) {

            byte[] bytes = IOUtils.toByteArray(s3Object.getObjectContent());

            Blob blob = new Blob(s3Object.getKey(), new ByteArrayInputStream(bytes), tika.detect(bytes));
            return Optional.of(blob);
        }

    }

    /*
    Get the key name and change below logic
     */
    @Override
    public void deleteAll() {
        amazonS3Client.deleteBucket(photoStorageBucket);
    }
}
