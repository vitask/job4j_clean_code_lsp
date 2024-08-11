package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        var result = false;
        List<MenuItem> parent = null;
        if (Objects.equals(parentName, Menu.ROOT)) {
            parent = rootElements;
        } else {
            var optionalItem = findItem(parentName);
            if (optionalItem.isPresent()) {
                parent = optionalItem.get().menuItem.getChildren();
            }
        }
        if (Objects.nonNull(parent)) {
            parent.add(new SimpleMenuItem(childName, actionDelegate));
            result = true;
        }
        return result;
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        var optionalItem = findItem(itemName);
        Optional<MenuItemInfo> result = Optional.empty();
        if (optionalItem.isPresent()) {
            var itemInfo = optionalItem.get();
            result = Optional.of(new MenuItemInfo(itemInfo.menuItem, itemInfo.number));
        }
        return result;
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        var result = new ArrayList<MenuItemInfo>();
        var dfsIterator = new DFSIterator();
        while (dfsIterator.hasNext()) {
            var itemInfo = dfsIterator.next();
            result.add(new MenuItemInfo(itemInfo.menuItem, itemInfo.number));
        }
        return result.iterator();
    }

    private Optional<ItemInfo> findItem(String name) {
        Optional<ItemInfo> result = Optional.empty();
        var iterator = new DFSIterator();
        while (iterator.hasNext()) {
            var itemInfo = iterator.next();
            if (Objects.equals(name, itemInfo.menuItem.getName())) {
                result = Optional.of(itemInfo);
            }
        }
        return result;
    }

    private static class SimpleMenuItem implements MenuItem {

        private final String name;
        private final List<MenuItem> children = new ArrayList<>();
        private final ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private final Deque<MenuItem> stack = new LinkedList<>();

        private final Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private record ItemInfo(MenuItem menuItem, String number) {
    }
}