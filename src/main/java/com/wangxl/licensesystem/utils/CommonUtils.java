package com.wangxl.licensesystem.utils;

import com.wangxl.licensesystem.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @ClassName CommonUtils
 * @Description : 基本工具类
 *
 * @Author : Wangxl
 * @Date : 2020/5/13 16:39
*/
@Slf4j
public class CommonUtils {

    //根据部署系统选择
    public static final boolean linux = true;


    private static FastDFSImage fastDFSImage = new FastDFSImage();
    /* 所有的字符串 */
    public static final String ALLSTR = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    //加密用到的key
    public static final String KEY = "asd.1234";
    /* 编码 */
    public static final String CHARSET = "UTF-8";
    /*匹配${}的正则,还有分组的概念*/
    private static Pattern escapresource = Pattern.compile("(\\$\\{)([\\w]+)(\\})");
    /* 请求次数 */
    public static final int REQ_COUNT = 5;
    /* 日期的默认格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /* 日期时间的默认格式 */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /* 秒 */
    public static final int SECOND = 1000;
    /*存放所有线程的容器*/
    public static Map<String, Map<String, Object>> THREAD_MAP = new Hashtable<String, Map<String, Object>>();

    /*本地存储图片的路径*/
    public static final String LOCAL_PATH = "file";

    private static CommonUtils commonUtils = null;

    private CommonUtils() {
    }

    public static CommonUtils getInstance() {
        if (commonUtils == null)
            commonUtils = new CommonUtils();
        return commonUtils;
    }

    /**
     * 返回一个32位长全大写的不重复的字符串
     *
     * @return
     */
    public String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public boolean isNotBlank(String string) {
        if (string != null && (!string.trim().equals(""))) {
            return true;
        }
        return false;
    }

    /**
     * 创建临时文件夹
     *
     * @return
     */
    public static File getNewTempFolder() {
        String tempPath = System.getProperty("java.io.tmpdir");
        File newFile = null;
        while (true) {
            synchronized (FileOperUtils.class) {
                newFile = new File(tempPath, "f" + System.currentTimeMillis());
                if (!newFile.exists()) {
                    newFile.mkdirs();
                    break;
                }
            }
        }
        return newFile;
    }

    public static void createFolder(String path) {
        File file = new File(path);
        if (file.isDirectory() && (!file.exists())) {
            file.mkdirs();
        }
    }
    /**
     * 上传图片
     * @param file
     * @return 图片文件名
     * @throws Exception
     */
/*	public static String uploadImage(MultipartFile file) throws IllegalStateException, IOException {
		String fileName = file.getOriginalFilename();
		String ext = "";
		if (fileName.lastIndexOf(".") != -1) {
			ext = fileName.substring(fileName.lastIndexOf("."));
		}
		String imageName = CommonUtils.getInstance().uuid() + ext;
		File image = new File(UnZipFileUtils.UPLOAD_PATH + imageName);
		FileOperUtils.createFolder(image.getParent());
		file.transferTo(image);
		return imageName;
	}*/

    /**
     * 上传附件至服务器
     *
     * @param multipartFile
     * @param users
     * @return 附件的url及文件名称
     * @throws Exception
     */
    public static String uploadFile(HttpServletRequest request, MultipartFile multipartFile, Users users) throws Exception {
        if (CommonUtils.linux == false) {
            try {
                if (multipartFile != null && (!multipartFile.getOriginalFilename().equals(""))) {
                    File temp = new File(request.getSession().getServletContext().getRealPath("/") + "/" + users.getPhotoPath());
                    if (temp.isFile()) {
                        temp.delete();
                    }
                    String savePath = request.getSession().getServletContext().getRealPath("/") + "/file";
                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String filename = timeStamp;
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    //获取item中的上传文件的输入流
                    InputStream in = multipartFile.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while ((len = in.read(buffer)) > 0) {
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //压缩图片
                    ImageCompress imageCompress = new ImageCompress();
                    try {
                        String fileBasePath = savePath + "/" + filename;
                        File file = new File(fileBasePath);// 读入文件
                        if (file.length() > 50000) { //大于5kB进行压缩, 压缩为290*320格式
                            String filePath = fileBasePath + ".png";
                            imageCompress.resize(fileBasePath, filePath, 290, 320);
                            file.delete();
                            File file2 = new File(filePath);
                            file2.renameTo(file);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.err.println("compress error! " + e);
                    }
                    //备份文件到服务器本地
                    String basePath2 = CommonUtils.getInstance().getProperties().getProperty("windowsSavePath", "E:/file");

                    System.out.println("JMY:" + basePath2);
                    File folder = new File(basePath2);
                    //如果文件夹不存在则创建
                    if (!folder.exists() && !folder.isDirectory()) {
                        log.info("不存在");
                        folder.mkdirs();
                    } else {
                        log.info("目录存在");
                    }
                    String oldUrl = users.getPhotoPath();
                    File temp2 = new File(basePath2 + "/" + oldUrl.substring(oldUrl.lastIndexOf("/") + 1));
                    System.out.println("JMY:" + basePath2 + "/" + oldUrl.substring(oldUrl.lastIndexOf("/") + 1));
                    if (temp2.isFile()) {
                        temp2.delete();
                    }
                    try {
                        FileInputStream input = new FileInputStream(savePath + "/" + filename);//可替换为任何路径何和文件名
                        FileOutputStream output = new FileOutputStream(basePath2 + "/" + filename);//可替换为任何路径何和文件名
                        int filein = input.read();
                        while (filein != -1) {
                            output.write(filein);
                            filein = input.read();
                        }
                        input.close();
                        output.close();
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                    users.setPhotoPath("file" + "/" + filename);
                    users.setPhotoName((multipartFile.getOriginalFilename()));
                    users.setPhotoSize((double) (multipartFile.getSize() / 1024 / 1024));
                    return users.getPhotoPath() + "," + users.getPhotoName();
                } else {
                    return ",";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ",";
            }
        } else {
            return uploadFile(multipartFile, users, 290, 320);
        }
    }

    public static String uploadFile(MultipartFile multipartFile, Users users, int width, int height) throws Exception {
        if (multipartFile != null && (!multipartFile.getOriginalFilename().equals(""))) {
            // 删除旧文件
            CommonUtils.deleteFile(users.getPhotoPath());

            // 上传新文件
            String fileName = multipartFile.getOriginalFilename();
            String preffix = fileName.substring(0, fileName.lastIndexOf("."));
            // preffix的长度小于3时，调用File.createTempFile会报错
            if (preffix.length() < 3)
                preffix += "11";
            String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            File file = File.createTempFile(preffix, suffix);
            multipartFile.transferTo(file);
            String url = fastDFSImage.uploadImage(file.getAbsolutePath(), width, height);
            file.deleteOnExit();

            users.setPhotoPath(url);
            users.setPhotoName(multipartFile.getOriginalFilename());
            users.setPhotoSize((double) (multipartFile.getSize() / 1024 / 1024));

            return users.getPhotoPath() + ',' + users.getPhotoName();
        } else {
            return ",";
        }
    }

    /**
     * 本地存储图片的路径
     * */
    public static String getLoalPath(HttpServletRequest request) {
        File temp = new File(request.getSession().getServletContext().getRealPath("/"));
        File file=new File(temp.getAbsolutePath() + "\\"+LOCAL_PATH+"\\");
        if(!file.exists()){
            file.mkdir();
        }
        return temp.getAbsolutePath() + "\\"+LOCAL_PATH+"\\";
    }

    /**
     * 删除文件
     */
    public static boolean deleteFileLoal(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                log.info("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            log.info("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }



    /**
     * 上传附件至服务器
     *
     * @param imageUrl 文件在本地的绝对路径
     * @return
     * @throws Exception
     */
    public static String uploadFile(String imageUrl) throws Exception {
        String url = fastDFSImage.uploadImage(imageUrl);
        return url;
    }

    /**
     * 删除服务器上的文件
     *
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public static boolean deleteFile(String fileUrl) throws Exception {
        if (StringUtils.isNotBlank(fileUrl))
            return fastDFSImage.deleteImage(fileUrl);
        return true;
    }

    /**
     * @author: Wangxl
     * @description: 专门用来处理点位符
     * @date :2019年3月20日 下午7:53:59
     * @modifier:
     * @modificationTime:
     * @description:
     */
    public static String replaceOperator(String source, Map<String, String> paramsMap) {
        if (paramsMap.size() == 0) {
            return source;
        }

        StringBuffer sb = new StringBuffer();
		/*将${wangxl}的值替换掉*/
        Matcher matcher = escapresource.matcher(source);
        while (matcher.find()) {
            if (paramsMap.get(matcher.group(2)) != null) {
                matcher.appendReplacement(sb, paramsMap.get(matcher.group(2)));
            }
        }
		
		/* 将尾巴加上去 */
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @author: Wangxl
     * @description: 通过配置文件中属性的键读取信息。
     * @date :2019年3月25日 下午6:19:17
     * @modifier:
     * @modificationTime:
     * @description:
     */
    public static String getPropertiesByKey(String keyWord) {
        Properties prop = new Properties();
        String value = null;
        try {
            InputStream inputStream = CommonUtils.class.getClassLoader().getResourceAsStream("server_url.properties");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            prop.load(bufferedReader);
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 上传附件至服务器
     *
     * @param multipartFile
     * @param users
     * @return 附件的url及文件名称
     * @throws Exception
     */
    public static String uploadFile(MultipartFile multipartFile, Users users) throws Exception {
        //System.out.print("========="+multipartFile.getOriginalFilename());
        if (multipartFile != null && (!multipartFile.getOriginalFilename().equals("")) &&
                (multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpg")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpeg")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".png")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith("mp4")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".gif")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".doc")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".docx")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".pdf"))) {
            // 删除旧文件
            if (users.getPhotoPath() != null) {
                CommonUtils.deleteFile(users.getPhotoPath());
            }

            // 上传新文件
            String fileName = multipartFile.getOriginalFilename();
            String preffix = fileName.substring(0, fileName.lastIndexOf("."));
            // preffix的长度小于3时，调用File.createTempFile会报错
            if (preffix.length() < 3)
                preffix += "11";
            String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            File file = File.createTempFile(preffix, suffix);
            multipartFile.transferTo(file);
            String url = fastDFSImage.uploadImage(file.getAbsolutePath());
            file.deleteOnExit();
            users.setPhotoPath(url);
            users.setPhotoName(multipartFile.getOriginalFilename());
            users.setPhotoSize((double) (multipartFile.getSize() / 1024));

            return users.getPhotoPath()+','+ users.getPhotoName();
        } else {
            return "";
        }
    }



    /**
     * 上传附件至服务器
     *
     * @param multipartFile
     * @return 附件的url
     * @throws Exception
     */
    public static String uploadFile(MultipartFile multipartFile) throws Exception {
        //System.out.print("========="+multipartFile.getOriginalFilename());
        if (multipartFile != null && (!multipartFile.getOriginalFilename().equals("")) &&
                (multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpg")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpeg")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".png")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith("mp4")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".gif")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".doc")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".docx")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")
                        || multipartFile.getOriginalFilename().toLowerCase().endsWith(".pdf"))) {

            // 上传新文件
            String fileName = multipartFile.getOriginalFilename();
            String preffix = fileName.substring(0, fileName.lastIndexOf("."));
            // preffix的长度小于3时，调用File.createTempFile会报错
            if (preffix.length() < 3)
                preffix += "11";
            String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            File file = File.createTempFile(preffix, suffix);
            multipartFile.transferTo(file);
            String url = "";
            if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpg")
            		||multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpeg")
            		||multipartFile.getOriginalFilename().toLowerCase().endsWith(".png")
            		||multipartFile.getOriginalFilename().toLowerCase().endsWith("mp4")
            		|| multipartFile.getOriginalFilename().toLowerCase().endsWith(".gif"))
			{
            	 url = fastDFSImage.uploadImage(file.getAbsolutePath(), false);
			}else {
				
				 url = fastDFSImage.uploadImage(file.getAbsolutePath(), true);
			}
            file.deleteOnExit();
            return url;
        } else {
            return "";
        }
    }


    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     *
     * @param obj1      进行属性比较的对象1
     * @param obj2      进行属性比较的对象2
     * @param ignoreArr 选择忽略比较的属性数组
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, String[] ignoreArr) {
        try {
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<String> ignoreList = null;
            if (ignoreArr != null && ignoreArr.length > 0) {
                // array转化为list
                ignoreList = Arrays.asList(ignoreArr);
            }
            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = obj1.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    if (ignoreList != null && ignoreList.contains(name)) {// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(obj1);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(obj2);
                    if (o1 instanceof Timestamp) {
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if (o2 instanceof Timestamp) {
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if (o1 == null && o2 == null) {
                        continue;
                    } else if (o1 == null && o2 != null) {
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                        continue;
                    }
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                    }
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读入properties文件
     * */
    public static String getProperties(String pathName,String propertyKey) throws IOException {
        InputStream insss =CommonUtils.class.getClassLoader().getResourceAsStream(pathName);
        Properties pss = new Properties();
        pss.load(insss);
        return pss.getProperty(propertyKey);
    }
    public Properties getProperties(){
        Properties p = new Properties();
        InputStream in = CommonUtils.class.getClassLoader().getResourceAsStream("server_url.properties");
        try {
            p.load(in);
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return p;
    }


}
