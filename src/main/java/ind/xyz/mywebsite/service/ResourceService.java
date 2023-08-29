package ind.xyz.mywebsite.service;

import ind.xyz.mywebsite.domain.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ResourceService {
    boolean addResource(Resource resource, MultipartFile multipartFile) throws Exception;

    void deleteResourceById(String id);

    void updateResource(Resource resource);

    Resource getResourceById(String id);

    List<Resource> getResources(Resource resource);
}

