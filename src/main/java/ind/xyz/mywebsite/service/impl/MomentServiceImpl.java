package ind.xyz.mywebsite.service.impl;

import ind.xyz.mywebsite.domain.Moment;
import ind.xyz.mywebsite.mapper.MomentMapper;
import ind.xyz.mywebsite.service.MomentService;
import ind.xyz.mywebsite.util.TimezoneUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MomentServiceImpl implements MomentService {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public void save(Moment moment) {
        moment.setId(UUID.randomUUID().toString());
        moment.setCreateTime(LocalDateTime.now(ZoneId.of("UTC")));
        moment.setUpdateTime(LocalDateTime.now(ZoneId.of("UTC")));
        SqlSession session = sqlSessionFactory.openSession();
        MomentMapper mapper = session.getMapper(MomentMapper.class);
        mapper.save(moment);
        session.commit();

        return;
    }

//    public Moment selectMomentById(String id) {
//        try (SqlSession session = sqlSessionFactory.openSession()) {
//            MomentMapper mapper = session.getMapper(MomentMapper.class);
//            return mapper.selectMomentById(id);
//        }
//    }
//
//    public void updateMoment(Moment moment) {
//        try (SqlSession session = sqlSessionFactory.openSession()) {
//            MomentMapper mapper = session.getMapper(MomentMapper.class);
//            mapper.updateMoment(moment);
//            session.commit();
//        }
//    }
//
//    public void deleteMoment(String id) {
//        try (SqlSession session = sqlSessionFactory.openSession()) {
//            MomentMapper mapper = session.getMapper(MomentMapper.class);
//            mapper.deleteMoment(id);
//            session.commit();
//        }
//    }

    public List<Moment> getAll(){
        SqlSession session = sqlSessionFactory.openSession();
        MomentMapper mapper = session.getMapper(MomentMapper.class);
        List<Moment> moments = mapper.getAll();
        session.close();
        // Set time by timezone
        return moments.stream().map(moment -> {
            Moment momentNew=new Moment();
            BeanUtils.copyProperties(moment,momentNew);
            momentNew.setCreateTime(TimezoneUtil.toTimezone(moment.getCreateTime(),moment.getTimezone()));
            return momentNew;
        }).collect(Collectors.toList());
    }
}
