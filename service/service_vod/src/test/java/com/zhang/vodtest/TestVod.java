package com.zhang.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws ClientException {
        String accessKeyId = "LTAI5tG6YDoFWWtNCx7UF3dL";
        String accessKeySecret ="S9pmKDZnveUCOUfcLqmveSZFedpg31";
        String title="test_video";
        String fileName="D:\\video\\6 - What If I Want to Move Faster.mp4";
        testUploadVideo(accessKeyId,accessKeySecret,title,fileName);
    }
    //获取视频播放地址
    public static void getPlayUrl() throws ClientException {
        //1、根据视频id获取视频播放地址
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tG6YDoFWWtNCx7UF3dL", "S9pmKDZnveUCOUfcLqmveSZFedpg31");
        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        //向request对象里面设置id值
        request.setVideoId("c48c939c56f34e139b3d001d504951f5");
        //调用初始化对象里面的方法传递request，获取数值
        response = client.getAcsResponse(request);  //response里面有视频的相关信息

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }

    //根据视频id获取凭证 -- 因为有些视频是加密的，光有地址没有用
    public static void getPlayAuth() throws ClientException {
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tG6YDoFWWtNCx7UF3dL", "S9pmKDZnveUCOUfcLqmveSZFedpg31");
        //创建获取视频凭证request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //向request设置id值
        request.setVideoId("c48c939c56f34e139b3d001d504951f5");
        //调用初始化对象的方法得到凭证
        response = client.getAcsResponse(request);
        System.out.println("playauth ="+response.getPlayAuth() );
    }

    /**
     * 本地文件上传接口
     *
     * @param accessKeyId     id值
     * @param accessKeySecret  秘钥
     * @param title           上传之后文件名称，阿里云中显示的文件名
     * @param fileName          本地文件路径和名称
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
       // System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
}
