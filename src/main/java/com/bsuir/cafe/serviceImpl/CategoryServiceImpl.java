package com.bsuir.cafe.serviceImpl;

import com.bsuir.cafe.JWT.JwtFilter;
import com.bsuir.cafe.POJO.Category;
import com.bsuir.cafe.constents.CafeConstants;
import com.bsuir.cafe.dao.CategoryDao;
import com.bsuir.cafe.service.CategoryService;
import com.bsuir.cafe.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
                //if(jwtFilter.isAdmin()){
                    if(validateCategoryMap(requestMap,false)){
                        categoryDao.save(getCategoryFromMap(requestMap,false));
                        return CafeUtils.getResponseEntity("Category added successfully!", HttpStatus.OK);
                    }
              /*  } else {
                    return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
                }*/
        } catch (Exception e){

        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            } else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
                if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                    log.info("Inside if");
                    return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
                }
                return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
          //  if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap,true)){
                   Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                   if (!optional.isEmpty()){
                        categoryDao.save(getCategoryFromMap(requestMap,true));
                        return CafeUtils.getResponseEntity("category updated successfully!",HttpStatus.OK);
                   } else {
                       return CafeUtils.getResponseEntity("Category ID doesn't exist!",HttpStatus.OK);
                   }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
          /*  } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }*/
        } catch (Exception e){

        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
