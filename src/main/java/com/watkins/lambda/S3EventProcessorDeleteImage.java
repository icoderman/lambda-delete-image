package com.watkins.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;

import java.io.IOException;
import java.net.URLDecoder;

public class S3EventProcessorDeleteImage  implements RequestHandler<S3Event, String> {

	@Override
	public String handleRequest(S3Event s3Event, Context context) {
		try {
			S3EventNotification.S3EventNotificationRecord record = s3Event.getRecords().get(0);

			String srcBucket = record.getS3().getBucket().getName();
			String srcKey = record.getS3().getObject().getKey();
			srcKey = URLDecoder.decode(srcKey, "UTF-8");
			String dstKey =  srcKey.replace("original/","thumbnail/");

			AmazonS3 s3Client = new AmazonS3Client();
			s3Client.deleteObject(srcBucket, dstKey);
			System.out.println("Successfully deleted thumbnail: " + srcBucket + "/" + dstKey);
			return "Ok";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
