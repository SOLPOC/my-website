package ind.xyz.mywebsite.controller;

import ind.xyz.mywebsite.domain.Blog;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.service.impl.BlogServiceImpl;
import ind.xyz.mywebsite.util.JsonUtil;
import ind.xyz.mywebsite.util.PropertiesUtil;
import ind.xyz.mywebsite.util.md.MdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
@CrossOrigin("*")
public class BlogController {
    @Autowired
    private BlogServiceImpl blogService;
    @PostMapping(value="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void save(@RequestParam("data")String blogString, @RequestParam("files") MultipartFile[] files) throws IOException {
//        MultipartFile[] multipartFiles=new MultipartFile[5];
//        if(optionalMultipartFiles.isPresent()) {
//            System.out.println(optionalMultipartFiles.get() );
//            multipartFiles=optionalMultipartFiles.get();
//        }
        blogService.save(PropertiesUtil.parseQueryString(blogString), files);
    }

    @PostMapping("/get")
    public Result get(@RequestBody Blog blog){
        List<Blog> blogs = blogService.get(blog);
        return Result.success(blogs);
    }

    @GetMapping("/getBlogById/{id}")
    public Result getBlogById(@PathVariable String id){
        Blog blog = blogService.getBlogById(id);
        return Result.success(blog);
    }

    @GetMapping("/getTCLMap")
    public Result getTagCategoryLanguageMap(){
        Map<String, Map<String, Integer>> tagCategoryLanguageMap = blogService.getTagCategoryLanguageMap();
        return Result.success(tagCategoryLanguageMap);
    }
}
