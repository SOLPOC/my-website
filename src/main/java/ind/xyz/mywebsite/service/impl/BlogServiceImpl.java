package ind.xyz.mywebsite.service.impl;

import cn.hutool.cache.CacheUtil;
import ind.xyz.mywebsite.config.FileTransferProperty;
import ind.xyz.mywebsite.domain.Blog;
import ind.xyz.mywebsite.domain.Moment;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.mapper.BlogMapper;
import ind.xyz.mywebsite.mapper.MomentMapper;
import ind.xyz.mywebsite.service.BlogService;
import ind.xyz.mywebsite.util.ConvertUtil;
import ind.xyz.mywebsite.util.HtmlUtil;
import ind.xyz.mywebsite.util.IOUtil;
import ind.xyz.mywebsite.util.file.FileUploadUtil;
import ind.xyz.mywebsite.util.file.FileUtil;
import ind.xyz.mywebsite.util.md.Md2HtmlUtil;
import ind.xyz.mywebsite.util.md.MdUtil;
import jdk.jfr.Category;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private FileTransferProperty fileTransferProperty;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Override
    public Result save(Blog blog, MultipartFile[] multipartFiles) throws IOException {
            blog.setId(UUID.randomUUID().toString());
            blog.setUrl(blog.getType().equals(".txt")?
                    fileTransferProperty.getDirectory()+"/blog/"+blog.getId()+".txt":
                    fileTransferProperty.getDirectory()+"/blog/"+blog.getId());
            if(blog.getType().equals(".txt")&&!StringUtils.hasLength(blog.getContent())){
                blog.setContent(getContentFroMF(blog,multipartFiles));
            }else if(blog.getType().equals(".md")){
                if(blog.getContent()==null) {
                    blog.setContent(getContentFroMF(blog, multipartFiles));
                }
                saveResource(blog,multipartFiles);
            }
            blog.setCreateTime(LocalDateTime.now());
            blog.setUpdateTime(LocalDateTime.now());
            SqlSession session = sqlSessionFactory.openSession();
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            mapper.save(blog);
            session.commit();
            session.close();
        if(backup(blog,multipartFiles)){
            return Result.success();
        }else{
            return Result.fail();
        }
    }

    public Map<String, Map<String,Integer>> getTagCategoryLanguageMap(){
        List<Blog> blogs = get(new Blog());
        Map<String ,Integer> tagMap=new HashMap<>();
        Map<String ,Integer> langMap=new HashMap<>();

        Map<String ,Integer> categoryMap=new HashMap<>();
        for(Blog blog:blogs){
            String[] tags = blog.getTag().split(" ");
            for(String tag:tags){
                if(tagMap.containsKey(tag)){
                    tagMap.put(tag,tagMap.get(tag)+1);
                }else{
                    tagMap.put(tag,1);
                }
            }
            if(langMap.containsKey(blog.getLanguage())){
                langMap.put(blog.getLanguage(),langMap.get(blog.getLanguage())+1);
            }else{
                langMap.put(blog.getLanguage(),1);
            }
            if(categoryMap.containsKey(blog.getCategory())){
                categoryMap.put(blog.getCategory(),categoryMap.get(blog.getCategory())+1);
            }else{
                categoryMap.put(blog.getCategory(),1);
            }
        }
        Map<String, Map<String,Integer>> res=new HashMap<>();
        res.put("language",langMap);
        res.put("category",categoryMap);
        res.put("tag",tagMap);
        return res;
    }

    public boolean backup(Blog blog,MultipartFile[] multipartFiles){
        boolean flag=false;
        if(blog.getType().equals(".txt")&& StringUtils.hasLength(blog.getContent())){
            String content=blog.getContent();
            flag= FileUtil.saveToServer(new ByteArrayInputStream(content.getBytes()), fileTransferProperty.getDirectory()+"/blog/", blog.getId() + ".txt");
        }else if(blog.getType().equals(".txt")&& !StringUtils.hasLength(blog.getContent())){
            Optional<MultipartFile> txt = Arrays.asList(multipartFiles).stream().filter(x -> x.getOriginalFilename().endsWith(".txt")).findFirst();
            if(txt.get()!=null) {
                flag = FileUtil.uploadToServer(txt.get(), fileTransferProperty.getDirectory()+"/blog", blog.getId()+".txt");
            }
        } else{
            if(blog.getContent()==null) {
                List<String> filenames = Arrays.stream(multipartFiles).map(x -> {
                    return x.getOriginalFilename().endsWith(".md") ? blog.getId() + ".md" : x.getOriginalFilename();
                }).collect(Collectors.toList());
                flag = FileUtil.BatchToServer(multipartFiles, fileTransferProperty.getDirectory() + "/blog" + blog.getId(), ConvertUtil.objectArray2StringArray(filenames.toArray()));
            }else{
                flag= FileUtil.saveToServer(new ByteArrayInputStream(blog.getContent().getBytes()), fileTransferProperty.getDirectory()+"/blog", blog.getId() + ".md");
            }
        }
        return flag;
    }

    public String   getContentFroMF(Blog blog, MultipartFile[] multipartFiles) throws IOException {
        if(blog.getType().equals(".txt")) {
            Optional<MultipartFile> first = Arrays.stream(multipartFiles).filter(x -> {
                return x.getOriginalFilename().endsWith(".txt");
            }).findFirst();
            InputStream inputStream = first.get().getInputStream();
            String string = ConvertUtil.inputStream2String(inputStream);
            inputStream.close();
            return string;
        }else{
            if(blog.getContent()==null) {
                Optional<MultipartFile> first = Arrays.stream(multipartFiles).filter(x -> {
                    return x.getOriginalFilename().endsWith(".md");
                }).findFirst();
                InputStream inputStream = first.get().getInputStream();
                String string = ConvertUtil.inputStream2String(inputStream);
                inputStream.close();
                return string;
            }
        }
        return null;
    }

    public List<Blog> get(Blog blog){
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        List<Blog> blogs = mapper.get(blog);
        blogs.stream().forEach(x->{
            // Turn content into html if .md
//            if(x.getType().equals(".md")){
//                String html = MdUtil.MdToHtmlForApiDoc(x.getContent());
//                System.out.println(html);
//                x.setContent(html);
//            }
        });
        return blogs;

    }

    public Blog getBlogById(String id){

        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        Blog blog = mapper.getBlogById(id);
        if(blog.getType().equals(".md")){
            String html = MdUtil.MdToHtmlForApiDoc(blog.getContent());
            String style="mixed";
            if(blog.getLanguage().equals("cn")){
                style=" font-family:'Times New Roman', '宋体', sans-serif;";
            }
            if(blog.getLanguage().equals("ja")){
                style=" font-family: 'Yu Mincho', sans-serif;";
            }
            if(blog.getLanguage().equals("en")){
                style=" font-family: 'Times New Roman', sans-serif;";
            }
            // Turn src link to base64
            List<String> allFilenames = IOUtil.getAllFilenames("D:/test/"+fileTransferProperty.getBlogDirectory() + "/" + blog.getId());
            if(allFilenames.size()!=0){
                for(String filename:allFilenames){
                    html = html.replaceFirst(filename,"data:image/png;base64,"+
//                            ConvertUtil.getImgStrToBase64("D:/test/"+fileTransferProperty.getBlogDirectory()+ "/"+blog.getId()+"/"+filename)
                            ConvertUtil.getImgStrToBase64(fileTransferProperty.getBlogDirectory()+ "/"+blog.getId()+"/"+filename)
                    );

                }


            }

            html = html.replace("*p_replaced*", style);
            html = html.replace("*li_replaced*", style);
            html= HtmlUtil.wrapWithArticle(html);
            blog.setContent(html);

        }
        return blog;
    }

    public static String wrapWithArticle(String html) {
        // 匹配 <blockquote>, <pre>, <table> 标签
        String regex = "<(blockquote|pre|table)\\b[^>]*>(.*?)</\\1>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);

        StringBuilder wrappedHtml = new StringBuilder();
        wrappedHtml.append("<article class=\"markdown-body\">");

        int lastIndex = 0;
        while (matcher.find()) {
            // 添加 <article> 标签之前的内容
            wrappedHtml.append(html.substring(lastIndex, matcher.start()));
            // 包裹标签内容
            wrappedHtml.append("<").append(matcher.group(1)).append(">")
                    .append(matcher.group(2))
                    .append("</").append(matcher.group(1)).append(">");
            lastIndex = matcher.end();
        }

        // 添加剩余的内容
        wrappedHtml.append(html.substring(lastIndex));
        wrappedHtml.append("</article>");

        return wrappedHtml.toString();
    }
    public boolean saveResource(Blog blog,MultipartFile[] multipartFiles) throws IOException {
        String fileDirectory=fileTransferProperty.getBlogDirectory()+"/"+blog.getId();
        boolean flag=true;
            for (MultipartFile multipartFile : multipartFiles) {
                if(multipartFile!=null) {
                    flag = flag && FileUtil.uploadToServer(multipartFile, fileDirectory, multipartFile.getOriginalFilename());
                }
            }
        return flag;
    }
}
