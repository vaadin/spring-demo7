package com.vaadin.framework7.samples.crud;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.framework7.samples.backend.DataService;
import com.vaadin.framework7.samples.backend.data.Availability;
import com.vaadin.framework7.samples.backend.data.Category;
import com.vaadin.framework7.samples.backend.data.Product;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * A form for editing a single product.
 * <p>
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductForm extends ProductFormDesign {

    private SampleCrudLogic viewLogic;
    private final FieldGroup fieldGroup = new FieldGroup();

    @Autowired
    private DataService dataService;

    @SpringComponent
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public static class ProductFormFactory {

        @Autowired
        private ApplicationContext context;

        public ProductForm createForm(SampleCrudLogic logic) {
            ProductForm form = context.getBean(ProductForm.class);
            form.init(logic);
            return form;
        }
    }

    private static class StockPriceConverter extends StringToIntegerConverter {

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale)
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }

        @Override
        public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws ConversionException {
            try {
                return super.convertToModel(value, Integer.class, locale);
            } catch (ConversionException e) {
                return 0;
            }
        }

    }

    private ProductForm() {
    }

    public void setCategories(Collection<Category> categories) {
        BeanItemContainer<Category> categoryContainer = new BeanItemContainer<>(Category.class);
        categoryContainer.addAll(categories);
        category.setContainerDataSource(categoryContainer);
    }

    public void editProduct(Product product) {
        if (product == null) {
            product = new Product();
        }
        fieldGroup.setItemDataSource(new BeanItem<>(product));
        delete.setEnabled(product.getId() != -1);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    @PostConstruct
    private void init() {
        addStyleName("product-form");

        availability.setContainerDataSource(new BeanItemContainer<>(Availability.class, Arrays.asList(Availability.values())));
        price.setConverter(new EuroConverter());
        fieldGroup.bind(price, "price");
        fieldGroup.bind(productName, "productName");
        productName.addValidator(new StringLengthValidator("Product name must have at least two characters",2,1024,false));

        fieldGroup.bind(availability, "availability");

        save.addClickListener(event -> onSave());

        cancel.addClickListener(event -> {
            fieldGroup.discard();
            viewLogic.cancelProduct();
        });
        delete.addClickListener(event -> onDelete());

        category.setItemCaptionPropertyId("name");

        fieldGroup.bind(category, "category");

        fieldGroup.bind(stockCount, "stockCount");
        stockCount.setConverter(new StockPriceConverter());
    }

    private void onSave() {
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {
            throw new RuntimeException(e);
        }
        Product product = ((BeanItem<Product>) fieldGroup.getItemDataSource()).getBean();
        dataService.updateProduct(product);
        viewLogic.saveProduct(product);
    }

    private void onDelete() {

        Product product = ((BeanItem<Product>) fieldGroup.getItemDataSource()).getBean();
        if (product != null) viewLogic.deleteProduct(product);
    }

    private void init(SampleCrudLogic logic) {
        this.viewLogic = logic;
    }

}
