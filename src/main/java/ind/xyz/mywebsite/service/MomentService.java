package ind.xyz.mywebsite.service;

import ind.xyz.mywebsite.domain.Moment;

import java.util.List;

public interface MomentService {
    List<Moment> getAll();
    void save(Moment moment);
}
