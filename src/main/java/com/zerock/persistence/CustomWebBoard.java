package com.zerock.persistence;

import com.zerock.domain.WebBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CustomWebBoard {

    public Page<Object[]> getCustomPage(String type, String keyword, Pageable page);
}
