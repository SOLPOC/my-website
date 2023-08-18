package ind.xyz.mywebsite.controller;

import ind.xyz.mywebsite.domain.Blog;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.service.impl.BlogServiceImpl;
import ind.xyz.mywebsite.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blog")
@CrossOrigin("*")
public class BlogController {
    @Autowired
    private BlogServiceImpl blogService;
    @PostMapping(value="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void save(@RequestPart("data")String blogString, @RequestPart("file")MultipartFile[] multipartFiles) throws IOException {
        // At front, there are two types file or text
        blogService.save(JsonUtil.fromJson(blogString,Blog.class),multipartFiles);
    }

    @GetMapping("/get")
    public Result get(@RequestBody Blog blog){
        List<Blog> blogs = blogService.get(blog);
        return Result.success(blogs);
    }
}
