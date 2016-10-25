package com.vaadin.spring.tutorial;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.EnableVaadinNavigation;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. This UI represents a browser window
 * where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Theme("valo")
@SpringUI
public class NavigableSpringTestUI extends UI {

    @Autowired
    private SpringTestViewContainer viewContainer;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(viewContainer);
    }

    @WebListener
    public static class SpringContextLoaderListener extends ContextLoaderListener {
    }

    @Configuration
    @EnableVaadinNavigation
    @EnableVaadin
    public static class SpringConfiguration {
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigableSpringTestUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }

}
