package com.time.memory.model;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.time.memory.model.impl.IAliUploadController;

/**
 * @author Qiu
 * @version V1.0
 * @Description:Ali上传
 * @date 2016/10/21 9:26
 */
public class AliUploadController implements IAliUploadController {
	private OSS oss;
	private String bucketName; // 上传到Bucket的名字
	private String objectKey; // 上传到OSS后的文件名
	private String uploadFilePath; // 本地上传路径
	private String accessKeySecret; // accessKeySecret
	private String accessKey; // accessKey
	private String bucketUrl; // 外网访问域名

	public AliUploadController(Context mContext) {
		try {
			this.bucketName = "sgjy";
			String bucketUrl = "http://oss-cn-beijing.aliyuncs.com";
			String accessKey = "LTAIjJ7ppqfpxvMd";
			String accessKeySecret = "qFG9UbST8GKYCgmbg8VTAmVlw2aJqi";//OK

			// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
			OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKey, accessKeySecret);

			ClientConfiguration conf = new ClientConfiguration();
			conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
			conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
			conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
			conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
			OSSLog.enableLog();
			oss = new OSSClient(mContext, bucketUrl, credentialProvider, conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ali上传
	 */
	@Override
	public void upLoad(String filePath, String objectKey, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) {
		this.uploadFilePath = filePath;
		this.objectKey = objectKey;
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
		OSSAsyncTask task = oss.asyncPutObject(put, completedCallback);
	}
}
