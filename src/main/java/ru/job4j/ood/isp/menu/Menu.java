package ru.job4j.ood.isp.menu;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Menu extends Iterable<Menu.MenuItemInfo> {

    String ROOT = null;

    boolean add(String parentName, String childName, ActionDelegate actionDelegate);

    Optional<MenuItemInfo> select(String itemName);

    record MenuItemInfo(String name, List<String> children, ActionDelegate actionDelegate, String number) {
        public MenuItemInfo(MenuItem menuItem, String number) {
            this(menuItem.getName(),
                    menuItem.getChildren().stream().map(MenuItem::getName).collect(Collectors.toList()),
                    menuItem.getActionDelegate(),
                    number);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MenuItemInfo that = (MenuItemInfo) o;
            return Objects.equals(name, that.name)
                    && Objects.equals(children, that.children)
                    && Objects.equals(actionDelegate, that.actionDelegate)
                    && Objects.equals(number, that.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, children, actionDelegate, number);
        }
    }
}
