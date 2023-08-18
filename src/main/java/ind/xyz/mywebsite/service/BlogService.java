package ind.xyz.mywebsite.service;

import ind.xyz.mywebsite.domain.Blog;
import ind.xyz.mywebsite.dto.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BlogService {
    Result save(Blog blog, MultipartFile[] multipartFiles) throws IOException;
}
