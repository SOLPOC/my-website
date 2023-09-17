package ind.xyz.mywebsite.controller;

import ind.xyz.mywebsite.config.FileTransferProperty;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/file")
@CrossOrigin("*")
public class FileController {
    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private FileTransferProperty fileTransferProperty;

    @PostMapping("/upload")
    public Result upload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("file") MultipartFile multipartFile) {
        return Result.success(fileService.upload(httpServletRequest, httpServletResponse,fileTransferProperty.getDirectory(), multipartFile));
    }

    @PostMapping("/uploadAll")
    public Result uploadAll(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("file") MultipartFile[] multipartFiles) {
        String[] directories=new String[multipartFiles.length];
        Arrays.fill(directories, fileTransferProperty.getDirectory());
        fileService.uploadAll(httpServletRequest, httpServletResponse,directories, multipartFiles);
        return Result.success();
    }

    @GetMapping("/download/{filename}")
    public Result download(HttpServletRequest request, HttpServletResponse response, @PathVariable String filename) {
        if (fileService.download(request, response,fileTransferProperty.getResourceDirectory(), filename,null)) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
