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
package com.vaadin.spring.tutorial;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for navigable views
 *
 * @author Vaadin Ltd
 */
public class TrivialNavigationComponent extends VerticalLayout implements View {

    @Autowired
    private AccessControlForViewBeans accessControl;

    @Autowired
    private Navigator navigator;

    public TrivialNavigationComponent(String caption) {

        setMargin(true);
        Label title = new Label(caption);
        title.setStyleName(ValoTheme.LABEL_SUCCESS + " " + ValoTheme.LABEL_BOLD);
        addComponent(title);
        addButton("login", "Log In", () -> accessControl.setPrivateViewAllowed(true), "Access Granted");
        addButton("logout", "Log Out", () -> accessControl.setPrivateViewAllowed(false), "Access Revoked");
        addButton("toPrivate", "Open Private", () -> navigator.navigateTo("private"), null);
        addButton("toPublic", "Open Public", () -> navigator.navigateTo("public"), null);
        addButton("toDefault", "Open Default", () -> navigator.navigateTo(""), null);
        addButton("toWrong", "Open Wrong", () -> navigator.navigateTo("Wrong"), null);

    }

    private void addButton(String id, String caption, Runnable action, String notificationText) {
        NativeButton button = new NativeButton(caption, clickEvent -> {
            action.run();
            if (notificationText != null) Notification.show(notificationText);
        });
        button.setId(id);
        addComponent(button);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
