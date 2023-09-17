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
import ind.xyz.mywebsite.util.file.FileUtil;
import ind.xyz.mywebsite.util.md.MdUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            }else if(blog.getType().equals(".md")&&!StringUtils.hasLength(blog.getContent())){
                blog.setContent(getContentFroMF(blog,multipartFiles));
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

    public boolean backup(Blog blog,MultipartFile[] multipartFiles){
        boolean flag=false;
        if(blog.getType().equals(".txt")&& StringUtils.hasLength(blog.getContent())){
            String content=blog.getContent();
            flag= FileUtil.saveToServer(new ByteArrayInputStream(content.getBytes()), fileTransferProperty.getDirectory()+"/blog/", blog.getId() + ".txt");
        }else if(blog.getType().equals(".txt")&& !StringUtils.hasLength(blog.getContent())){
            Optional<MultipartFile> txt = Arrays.asList(multipartFiles).stream().filter(x -> x.getOriginalFilename().endsWith(".txt")).findFirst();
            if(txt.get()!=null) {
                flag = FileUtil.uploadToServer(txt.get(), fileTransferProperty.getDirectory()+"/blog/", blog.getId()+".txt");
            }
        } else{
            List<String> filenames = Arrays.stream(multipartFiles).map(x -> {
                return x.getOriginalFilename().endsWith(".md")? blog.getId()+".md" : x.getOriginalFilename();
            }).collect(Collectors.toList());
            flag = FileUtil.BatchToServer(multipartFiles, fileTransferProperty.getDirectory()+"/blog/"+blog.getId(), ConvertUtil.objectArray2StringArray(filenames.toArray()));
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
            Optional<MultipartFile> first = Arrays.stream(multipartFiles).filter(x -> {
                return x.getOriginalFilename().endsWith(".md");
            }).findFirst();
            InputStream inputStream = first.get().getInputStream();
            String string = ConvertUtil.inputStream2String(inputStream);
            inputStream.close();
            return string;
        }

    }

    public List<Blog> get(Blog blog){
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        List<Blog> blogs = mapper.get(blog);
        blogs.stream().forEach(x->{
            // Turn content into html if .md
            if(x.getType().equals(".md")){
                String html = MdUtil.MdToHtmlForApiDoc(x.getContent());
                x.setContent(html);
            }
        });
        return blogs;

    }

    public Blog getBlogById(String id){
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        Blog blog = mapper.getBlogById(id);
        return blog;
    }
}
