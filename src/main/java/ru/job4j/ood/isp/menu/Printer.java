package ru.job4j.ood.isp.menu;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Printer implements MenuPrinter {

    private final static String INDENT_CHARACTER = "-";
    private final static int INDENT_SIZE = 2;
    private final PrintStream output;

    public Printer(PrintStream output) {
        this.output = output;
    }

    @Override
    public void print(Menu menu) {
        var printed = new HashSet<Menu.MenuItemInfo>();
        menu.forEach(menuItem -> printItemMenu(menu, menuItem, 0, printed));
    }

    private void printItemMenu(Menu menu, Menu.MenuItemInfo item, int indentCount, Set<Menu.MenuItemInfo> printed) {
        if (printed.add(item)) {
            output.print(INDENT_CHARACTER.repeat(indentCount));
            output.printf("%s %s%n", item.number(), item.name());
            for (String s : item.children()) {
                Optional<Menu.MenuItemInfo> select = menu.select(s);
                if (select.isPresent()) {
                    Menu.MenuItemInfo childItemInfo = select.get();
                    printItemMenu(menu, childItemInfo, indentCount + INDENT_SIZE, printed);
                }
            }
        }
    }
}
