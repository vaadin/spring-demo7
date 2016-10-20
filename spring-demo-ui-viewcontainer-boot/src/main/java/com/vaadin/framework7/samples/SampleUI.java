package com.vaadin.framework7.samples;

import com.vaadin.spring.annotation.EnableVaadinNavigation;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.framework7.samples.about.AboutView;
import com.vaadin.framework7.samples.authentication.AccessControl;
import com.vaadin.framework7.samples.authentication.LoginScreen;
import com.vaadin.framework7.samples.crud.SampleCrudView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
@EnableVaadinNavigation
public class SampleUI extends UI {

    @Autowired
    private AccessControl accessControl;

    private Menu menu;

    @Autowired
    private MyViewContainer viewContainer;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("My");
        getNavigator().navigateTo(SampleCrudView.VIEW_NAME);
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, this::showMainView));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);

        HorizontalLayout layout = new HorizontalLayout();
        setContent(layout);

        layout.setStyleName("main-screen");

        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        getNavigator().setErrorView(ErrorView.class);

        menu = new Menu(getNavigator());
        // View are registered automatically by Vaadin Spring support
        menu.addViewButton(SampleCrudView.VIEW_NAME, SampleCrudView.VIEW_NAME,
                FontAwesome.EDIT);
        menu.addViewButton(AboutView.VIEW_NAME, AboutView.VIEW_NAME,
                FontAwesome.INFO_CIRCLE);

        getNavigator().addViewChangeListener(new ViewChangeHandler());

        layout.addComponent(menu);
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1);
        layout.setSizeFull();

        getNavigator().navigateTo(SampleCrudView.VIEW_NAME);
    }

    public static SampleUI get() {
        return (SampleUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    private class ViewChangeHandler implements ViewChangeListener {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    }
}
