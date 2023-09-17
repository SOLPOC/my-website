package ind.xyz.mywebsite.mapper;

import ind.xyz.mywebsite.domain.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogMapper {
    public void save(Blog blog);
    List<Blog> getAll();
    List<Blog> get(Blog blog);

    Blog getBlogById(String id);
}
