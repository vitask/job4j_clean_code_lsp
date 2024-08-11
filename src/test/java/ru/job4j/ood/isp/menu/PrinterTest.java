package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;

class PrinterTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenPrintDailyDutyToDo() {
        var menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        menu.add("Сходить в магазин", "Забрать посылку", STUB_ACTION);
        var output = new ByteArrayOutputStream();
        var printer = new Printer(new PrintStream(output));
        printer.print(menu);
        var actual = output.toString();
        var expected = """
                1. Сходить в магазин
                --1.1. Купить продукты
                ----1.1.1. Купить хлеб
                ----1.1.2. Купить молоко
                --1.2. Забрать посылку
                2. Покормить собаку
                """;
        assertThat(actual).isEqualTo(expected);
    }
}