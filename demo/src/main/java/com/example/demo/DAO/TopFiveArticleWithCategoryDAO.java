package com.example.demo.DAO;

import com.example.demo.DTO.TopFiveArticleWithCategoryDTO;
import org.hibernate.SQLQuery;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class TopFiveArticleWithCategoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(TopFiveArticleWithCategoryDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<TopFiveArticleWithCategoryDTO> listTopFiveArticleWithCategory(){
        String sql = "select a.category_id, b.categoryName, a.articleId, a.title from master.articles a\n" +
                "left join master.category b\n" +
                "on a.category_id = b.categoryId";
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class);
        System.out.println(sqlQuery);
        Query query = sqlQuery.setResultTransformer(Transformers.aliasToBean(TopFiveArticleWithCategoryDTO.class));

        System.out.println(query);
        List<TopFiveArticleWithCategoryDTO> list = query.getResultList();
        entityManager.clear();
        return list;
    }
}
