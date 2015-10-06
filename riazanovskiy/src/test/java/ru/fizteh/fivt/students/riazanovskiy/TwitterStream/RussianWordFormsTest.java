package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import org.junit.Assert;
import org.junit.Test;

public class RussianWordFormsTest {

    @Test
    public void testGetWordForm() throws Exception {
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 0));
        Assert.assertEquals("минуту", RussianWordForms.getWordForm("минута", 1));
        Assert.assertEquals("минуты", RussianWordForms.getWordForm("минута", 2));
        Assert.assertEquals("минуты", RussianWordForms.getWordForm("минута", 3));
        Assert.assertEquals("минуты", RussianWordForms.getWordForm("минута", 4));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 5));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 9));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 10));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 11));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 12));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 16));
        Assert.assertEquals("минуту", RussianWordForms.getWordForm("минута", 21));
        Assert.assertEquals("минут", RussianWordForms.getWordForm("минута", 111));
        Assert.assertEquals("минуту", RussianWordForms.getWordForm("минута", 121));
        Assert.assertEquals("минуты", RussianWordForms.getWordForm("минута", 123));
    }
}
