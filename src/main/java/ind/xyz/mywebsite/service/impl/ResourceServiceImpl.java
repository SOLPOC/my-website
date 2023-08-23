package ind.xyz.mywebsite.service.impl;

import ind.xyz.mywebsite.config.FileTransferProperty;
import ind.xyz.mywebsite.domain.Resource;
import ind.xyz.mywebsite.mapper.ResourceMapper;
import ind.xyz.mywebsite.service.ResourceService;
import ind.xyz.mywebsite.util.file.FileVerifyUtil;
import ind.xyz.mywebsite.util.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private FileTransferProperty fileTransferProperty;

    @Override
    public void addResource(Resource resource, MultipartFile multipartFile) throws Exception {
        String id=UUID.randomUUID().toString();
        resource.setId(id);
        resource.setName(multipartFile.getOriginalFilename());
        resource.setSize(getSizeFromMultipartFile(multipartFile));
        resource.setType(multipartFile.getContentType());
        resource.setUploadTime(LocalDateTime.now());
        resource.setVerification(FileVerifyUtil.getShaFromInputStream(multipartFile.getInputStream()));
        String filename=id+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        resource.setUrl(fileTransferProperty.getResourceDirectory()+"/"+filename);
        boolean flag = FileUtil.uploadToServer(multipartFile, fileTransferProperty.getResourceDirectory(), filename);
        resourceMapper.insertResource(resource);

    }

    @Override
    public void deleteResourceById(String id) {
        resourceMapper.deleteResourceById(id);
    }

    @Override
    public void updateResource(Resource resource) {
        resourceMapper.updateResource(resource);
    }

    @Override
    public Resource getResourceById(String id) {
        return resourceMapper.getResourceById(id);
    }

    @Override
    public List<Resource> getResources(Resource resource) {
        return resourceMapper.getResources(resource);
    }

    /**
     * The size in resource has unit
     * @param multipartFile
     * @return
     */
    public String getSizeFromMultipartFile(MultipartFile multipartFile){
            long size= multipartFile.getSize();
            if (size <= 0) {
                return "0";
            }
            final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public boolean download(HttpServletRequest request, HttpServletResponse response,String id){
        Resource resource = getResourceById(id);
            fileService.download(
                    request,
                    response,
                    fileTransferProperty.getResourceDirectory(),
                    resource.getUrl().substring(resource.getUrl().lastIndexOf("/")),
                    resource.getName()
                    );
        return true;
    }
}
