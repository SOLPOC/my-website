package ind.xyz.mywebsite.controller;

import ind.xyz.mywebsite.domain.Moment;
import ind.xyz.mywebsite.dto.Result;
import ind.xyz.mywebsite.service.impl.MomentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moment")
@CrossOrigin("*")
public class MomentController {
    @Autowired
    private MomentServiceImpl momentService;
    @GetMapping("/getAll")
    public Result getAll(){
        return Result.success(momentService.getAll());
    }
    @PostMapping("/save")
    public Result save(@RequestBody Moment moment){
        momentService.save(moment);
        return Result.success();
    }
//    @DeleteMapping
//    public
}
