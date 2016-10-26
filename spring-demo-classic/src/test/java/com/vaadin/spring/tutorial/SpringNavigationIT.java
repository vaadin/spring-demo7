/*
 * Copyright 2000-2016 Vaadin Ltd.
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

import com.vaadin.demo.testutil.AbstractDemoTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Vaadin Ltd
 */
public class SpringNavigationIT extends AbstractDemoTest {

    @Before
    public void setUp() {
        open();
    }

    @Test
    public void testPageContent() {
        SearchContext context = getContext();
        Assert.assertEquals("Default View title", "Default", getTitleText());
        Set<String> buttonTitles = context.findElements(By.tagName("button")).stream().map(WebElement::getText)
                .collect(Collectors.toSet());
        List<String> expectedButtonsTitles = Arrays.asList(
                "Log In", "Log Out", "Open Private", "Open Public", "Open Default", "Open Wrong"
        );
        Assert.assertEquals("Button titles", new HashSet<>(expectedButtonsTitles), buttonTitles);
    }

    @Test
    public void testSuccess() {
        SearchContext context = getContext();
        context.findElement(By.id("login")).click();
        context.findElement(By.id("toPublic")).click();

        checkErrorIndicator(getToPublicButton(), false);
        Assert.assertEquals("View title", "Public", getTitleText());

        getToPrivateButton().click();

        checkErrorIndicator(getToPrivateButton(), false);
        Assert.assertEquals("View title", "Private", getTitleText());
    }

    @Test
    public void testDisallowedPage() {
        SearchContext context = getContext();
        context.findElement(By.id("login")).click();
        context.findElement(By.id("logout")).click();
        getToPublicButton().click();

        checkErrorIndicator(getToPublicButton(), false);
        Assert.assertEquals("View title", "Public", getTitleText());

        getToPrivateButton().click();

        checkErrorIndicator(getToPrivateButton(), true);
        Assert.assertEquals("View title", "Public", getTitleText());
    }

    @Test
    public void testWrongPage() {
        SearchContext context = getContext();
        context.findElement(By.id("login")).click();
        context.findElement(By.id("toWrong")).click();

        checkErrorIndicator(context.findElement(By.id("toWrong")), true);
        Assert.assertEquals("View title", "Default", getTitleText());
    }

    private void checkErrorIndicator(WebElement element, boolean shouldBePresent) {
        boolean present = 1 == element.findElements(By.className("v-errorindicator")).size();
        Assert.assertEquals("Error indicator", shouldBePresent, present);
    }

    private WebElement getToPrivateButton() {
        return getContext().findElement(By.id("toPrivate"));
    }

    private WebElement getToPublicButton() {
        return getContext().findElement(By.id("toPublic"));
    }

    private String getTitleText() {
        return getContext().findElement(By.id("title")).getText();
    }
}
