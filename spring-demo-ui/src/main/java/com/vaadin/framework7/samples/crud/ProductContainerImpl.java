/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.framework7.samples.crud;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.framework7.samples.backend.data.Product;
import com.vaadin.framework7.samples.backend.repository.ProductRepository;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

/**
 * TODO class description
 *
 * @author Vaadin Ltd
 */
@Component
@UIScope
@Transactional
public class ProductContainerImpl extends BeanContainer<Integer, Product> implements ProductContainer {

    @Autowired
    private ProductRepository productRepository;

    public ProductContainerImpl() throws IllegalArgumentException {
        super(Product.class);
        setBeanIdProperty("id");
    }

    @PostConstruct
    public void init() {
        List<Product> all = productRepository.findAll();
        addAll(all);
    }
}
