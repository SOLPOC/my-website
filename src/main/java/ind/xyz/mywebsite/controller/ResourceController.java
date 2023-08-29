package ind.xyz.mywebsite.controller;

import ind.xyz.mywebsite.domain.Blog;
import ind.xyz.mywebsite.domain.Resource;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.service.impl.FileServiceImpl;
import ind.xyz.mywebsite.service.impl.ResourceServiceImpl;
import ind.xyz.mywebsite.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/resource")
@CrossOrigin("*")
public class ResourceController {
    @Autowired
    private ResourceServiceImpl resourceService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


    @GetMapping("/get")
    public Result getResources(@RequestBody Resource resource) {
        return Result.success(resourceService.getResources(resource));
    }

    @PostMapping(value="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result insertResource(@RequestPart("data")String resourceString, @RequestPart("file") MultipartFile multipartFile) throws Exception {
        boolean flag = resourceService.addResource(JsonUtil.fromJson(resourceString, Resource.class), multipartFile);
        return flag? Result.success(): Result.fail();
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
         resourceService.download(request,response,id);
    }

}
