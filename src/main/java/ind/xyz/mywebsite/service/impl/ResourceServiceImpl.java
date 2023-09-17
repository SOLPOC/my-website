package ind.xyz.mywebsite.service.impl;

import ind.xyz.mywebsite.config.FileTransferProperty;
import ind.xyz.mywebsite.domain.Resource;
import ind.xyz.mywebsite.mapper.ResourceMapper;
import ind.xyz.mywebsite.service.ResourceService;
import ind.xyz.mywebsite.util.ConvertUtil;
import ind.xyz.mywebsite.util.file.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    /**
     * Fill in Resource and save file
     * @param resource
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @Override
    public boolean addResource(Resource resource, MultipartFile multipartFile) throws Exception {
        String id=UUID.randomUUID().toString();
        resource.setId(id);
        resource.setName(multipartFile.getOriginalFilename());
        resource.setSize(getSizeFromMultipartFile(multipartFile));
        resource.setType(multipartFile.getContentType());
        resource.setUploadTime(LocalDateTime.now());
        resource.setVerification(FileVerifyUtil.getShaFromInputStream(multipartFile.getInputStream()));
        resource.setUrl(fileTransferProperty.getResourceDirectory()+"/"+id);
        FileUtil.saveToServer(multipartFile.getInputStream(), fileTransferProperty.getResourceDirectory(),resource.getId());
//        String read = FileReadUtil.read("D:/test/"+fileTransferProperty.getResourceDirectory()+"/"+"temp");
//        System.out.println(read.length());
//        String string = FileAESUtil.encrypt(read);
//        System.out.println(string.length());
//        boolean flag = FileUtil.saveToServer(string, fileTransferProperty.getResourceDirectory(), id);
//        if(!flag){
//            return false;
//        }
        Integer integer = resourceMapper.insertResource(resource);
        return integer==1;
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
     * The size in Resource has unit
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

/*    public void download(HttpServletRequest request, HttpServletResponse response,String id){
        Resource resource = getResourceById(id);
            fileService.download(
                    request,
                    response,
                    fileTransferProperty.getResourceDirectory(),
                    resource.getUrl().substring(resource.getUrl().lastIndexOf("/")),
                    resource.getName()
                    );
    }*/

    /**
     * donwlaod encrypted file, decrypted to tempDir -> download -> remove
     * @param request
     * @param response
     * @param id
     */
        public void download(HttpServletRequest request, HttpServletResponse response,String id) throws Exception {
            Resource resource = getResourceById(id);
            // TODO
//            File file=new File("D:/test/"+fileTransferProperty.getResourceDirectory()+"/"+resource.getId());
//            String encryption = FileReadUtil.read(file);
//            System.out.println(encryption.length());
//            String decryption = FileAESUtil.decrypt(encryption);
//            System.out.println(decryption.length());
//            File temp=new File(fileTransferProperty.getTempDirectory()+resource.getId());
//            FileUtil.saveToServer(decryption,fileTransferProperty.getTempDirectory(),resource.getName());
            fileService.download(
                    request,
                    response,
                    fileTransferProperty.getResourceDirectory(),
                    resource.getId(),
                    resource.getName()
                    );
//            temp.delete();
    }

    public String  getImageUrl(String id) throws Exception {
        Resource resource = getResourceById(id);
        // TODO
//        File file=new File("D:/test/"+fileTransferProperty.getResourceDirectory()+"/"+ resource.getId());
//        String encryption = FileReadUtil.read(file);
//        String decryption = FileEncryptUtil.decryptByAES(encryption);
//        File temp=new File(fileTransferProperty.getTempDirectory()+"/"+ resource.getId());
//        FileUtil.saveToServer(decryption,fileTransferProperty.getTempDirectory(), resource.getId());
        // TODO
//        return     "D:/test/"+fileTransferProperty.getResourceDirectory()+ "/"+resource.getId();
        System.out.println(ConvertUtil.getImgStrToBase64("D:/test/"+fileTransferProperty.getResourceDirectory()+ "/"+resource.getId()));
        return ConvertUtil.getImgStrToBase64("D:/test/"+fileTransferProperty.getResourceDirectory()+ "/"+resource.getId());
    }
}
