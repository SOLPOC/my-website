package ind.xyz.mywebsite.mapper;

import ind.xyz.mywebsite.domain.Moment;

import java.util.List;

public interface MomentMapper {
     List<Moment> getAll();
     void save(Moment moment);

}
