package ind.xyz.mywebsite.service;

import ind.xyz.mywebsite.domain.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    void addResource(Resource resource, MultipartFile multipartFile);

    void deleteResourceById(String id);

    void updateResource(Resource resource);

    Resource getResourceById(String id);

    List<Resource> getResources(Resource resource);
}

