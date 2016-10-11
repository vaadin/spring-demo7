package com.vaadin.framework7.samples.crud;

import java.math.BigDecimal;
import java.util.*;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToCollectionConverter;
import com.vaadin.framework7.samples.backend.data.Availability;
import com.vaadin.framework7.samples.backend.data.Category;
import com.vaadin.framework7.samples.backend.data.Product;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductGrid extends Grid {

    public static final Map<Object,String> styleByColumn = new HashMap<>();
    static {
        styleByColumn.put("price","align-right");
        styleByColumn.put("stockCount","align-right");
    }
    public ProductGrid() {
        setSizeFull();
        setSelectionMode(SelectionMode.SINGLE);
        addColumn("id", Integer.class);
        addColumn("productName", String.class).setSortable(false);
        addColumn("price", BigDecimal.class);
        setCellStyleGenerator(cell -> styleByColumn.get(cell.getPropertyId()));


        addColumn("availability",Availability.class).setRenderer(new HtmlRenderer())
                .setConverter(new Converter<String, Availability>() {
                    @Override
                    public Availability convertToModel(String value, Class<? extends Availability> targetType, Locale locale) throws ConversionException {
                        return null;
                    }

                    @Override
                    public String convertToPresentation(Availability value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                        return getTrafficLightIconHtml(value) + " " + value.name();
                    }

                    @Override
                    public Class<Availability> getModelType() {
                        return Availability.class;
                    }

                    @Override
                    public Class<String> getPresentationType() {
                        return String.class;
                    }
                });
        
        addColumn("stockCount", Integer.class);
        addColumn("category",Set.class).setConverter(new StringToCollectionConverter(", "){
            @Override
            public String convertToPresentation(Collection value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                Category[] categories = ((Collection<Category>)value).toArray(new Category[value.size()]);
                Arrays.sort(categories,Comparator.comparing(Category::getId));
                return super.convertToPresentation(Arrays.asList(categories), targetType, locale);
            }
        }).setSortable(false);
    }

    private String getTrafficLightIconHtml(Availability availability) {
        String color = "";
        if (availability == Availability.AVAILABLE) {
            color = "#2dd085";
        } else if (availability == Availability.COMING) {
            color = "#ffc66e";
        } else if (availability == Availability.DISCONTINUED) {
            color = "#f54993";
        }

        String iconCode = "<span class=\"v-icon\" style=\"font-family: "
                + FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
                + "\">&#x"
                + Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
                + ";</span>";
        return iconCode;
    }

    public Product getSelectedRow() {
        Object id = ((SelectionModel.Single) getSelectionModel()).getSelectedRow();
        return id == null? null : ((BeanItem<Product>)getContainerDataSource().getItem(id)).getBean();
    }

}
