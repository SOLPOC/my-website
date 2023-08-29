package ind.xyz.mywebsite.mapper;

import ind.xyz.mywebsite.domain.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface ResourceMapper {
    Integer insertResource(Resource resource);

    void deleteResourceById(String id);

    void updateResource(Resource resource);

    List<Resource> getResources(Resource resource);

    Resource getResourceById(String id);
}
