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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
//    @PostMapping(value="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Result save(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        MultipartFile[] multipartFiles=request.getParameter("files");
//
//
//        blogString = java.net.URLDecoder.decode(blogString, "UTF-8");
//        return  blogService.save(PropertiesUtil.parseQueryString(blogString), multipartFiles);
//    }

    @PostMapping(value="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result save(@RequestParam("data")String blogString, @RequestParam("files") Optional<MultipartFile[]> optionalMultipartFiles) throws IOException {
        MultipartFile[] multipartFiles=new MultipartFile[5];
        if(optionalMultipartFiles.isPresent()) {
            System.out.println(optionalMultipartFiles.get() );
            multipartFiles=optionalMultipartFiles.get();
        }

        blogString = java.net.URLDecoder.decode(blogString, "UTF-8");
        blogString= java.net.URLDecoder.decode(blogString, "UTF-8");
        return  blogService.save(PropertiesUtil.parseQueryString(blogString), multipartFiles);
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
