package com.atai.oss.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.atai.oss.entity.ReadData;
import com.atai.oss.listener.ExcelListener;
import com.atai.oss.service.OssService;
import com.atai.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 上传头像服务实现类
 *
 * @author ZengJinming
 * @time 2020-04-03
 */

@Service
public class OssServiceImpl implements OssService {
    // TODO 如果是结果集将结果集上传到Redis缓存中
    // 上传文件(数据集、结果集和头像)到oss
    @Override
    public String uploadFile(MultipartFile file, RedisTemplate redisTemplate) {
        // 工具类获取值
        String endPoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1 在文件名称里面添加随机唯一的值  防止后面上传的覆盖之前的
            // replaceAll去掉uuid的横杠
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // yuy76t5rew01.jpg
            fileName = uuid + fileName;

            //2 把文件按照日期进行分类
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //拼接
            fileName = datePath + "/" + fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://atai-bigdata.oss-cn-beijing.aliyuncs.com/dasasdasd51as53d5125f01.jpg
            String url = "https://" + bucketName + "." + endPoint + "/" + fileName;
            // 上传保存到redis中
            downloadOssFile(url, redisTemplate);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeFile(String url) {
        //读取配置信息
        String endPoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        // 删除文件。
        String host = "https://" + bucketName + "." + endPoint + "/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public String downloadOssFile(String url, RedisTemplate redisTemplate) throws IOException {
        String endPoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        String host = "https://" + bucketName + "." + endPoint + "/";
        String objectName = url.substring(host.length());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream contentIs = ossObject.getObjectContent();

        if (contentIs != null) {
            if (url.endsWith(".xls") || url.endsWith(".xlsx")) {
                // 将读取的excel内容保存到redis中
                ExcelReader excelReader = EasyExcel.read(contentIs, ReadData.class, new ExcelListener(url, redisTemplate)).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                excelReader.read(readSheet);
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            } else if (url.endsWith(".txt")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(contentIs));
                String line;
                ArrayList<String> list = new ArrayList<>();
                // 将读取的txt内容保存到redis中
                BoundListOperations listOps = redisTemplate.boundListOps(url);
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                listOps.rightPushAll(list);
//                redisTemplate.expire(url, 30, TimeUnit.SECONDS);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            contentIs.close();
        }

        // 关闭OSSClient。
        ossClient.shutdown();
        return null;
    }
}
