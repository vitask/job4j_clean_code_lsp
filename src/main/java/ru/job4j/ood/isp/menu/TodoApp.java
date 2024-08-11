package ru.job4j.ood.isp.menu;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.stream.IntStream;

public class TodoApp {
    private static final String INPUT_TASK = "Введите описание для новой задачи";
    private static final String INPUT_PARENT_TASK = "Введите имя задачи, к которой добавить подпункт";
    private static final String INPUT_TASK_FOR_EXECUTION = "Введите имя задачи для выполнения";
    private static final String TASK_NOT_FOUND = "Задача не найдена";
    private static final String MENU = "Меню";
    private static final String ADD_TASK = "Добавить новую задачу";
    private static final String ADD_SUBTASK = "Добавить новую подзадачу";
    private static final String EXECUTE_TASK = "Выполнить задачу";
    private static final String PRINT_TODO = "Вывести TODO";
    private static final ActionDelegate DEFAULT_ACTION = () -> System.out.println("Some action");

    private final PrintStream output;
    private final Menu menu;
    private final Scanner inputScanner;
    private final MenuItem[] appMenu;
    private final Printer printer;

    public TodoApp(PrintStream output, InputStream input) {
        this.output = output;
        this.inputScanner = new Scanner(input);
        this.menu = new SimpleMenu();
        this.printer = new Printer(this.output);
        this.appMenu = new MenuItem[] {
                new MenuItem(ADD_TASK, this::addToRoot),
                new MenuItem(ADD_SUBTASK, this::addToParent),
                new MenuItem(EXECUTE_TASK, this::executeTask),
                new MenuItem(PRINT_TODO, this::printTodo)
        };
    }

    private void addToRoot() {
        output.println(INPUT_TASK);
        var taskName = inputScanner.nextLine();
        menu.add(Menu.ROOT, taskName, DEFAULT_ACTION);
    }

    private void addToParent() {
        output.println(INPUT_PARENT_TASK);
        var parent = inputScanner.nextLine();
        output.println(INPUT_TASK);
        var taskName = inputScanner.nextLine();
        menu.add(parent, taskName, DEFAULT_ACTION);
    }

    private void executeTask() {
        output.println(INPUT_TASK_FOR_EXECUTION);
        var taskName = inputScanner.nextLine();
        var optionalItem = menu.select(taskName);
        if (optionalItem.isPresent()) {
            optionalItem.get().actionDelegate().delegate();
        } else {
            output.println(TASK_NOT_FOUND);
        }
    }

    private void printTodo() {
        printer.print(menu);
    }

    private void printAppMenu() {
        output.println(MENU);
        IntStream.range(0, appMenu.length)
                .forEach(i -> output.printf("%d. %s%n", i + 1, appMenu[i].name()));
    }

    private void start() {
        while (true) {
            printAppMenu();
            try {
                int numMenu = Integer.parseInt(inputScanner.nextLine()) - 1;
                if (numMenu >= 0 && numMenu < appMenu.length) {
                    appMenu[numMenu].action.doAction();
                } else {
                    output.println("Некорректный выбор, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                output.println("Введите число.");
            }
            output.println();
        }
    }

    public static void main(String[] args) {
        var todo = new TodoApp(System.out, System.in);
        todo.start();
    }

    private interface Action {
        void doAction();
    }

    private record MenuItem(String name, Action action) {
    }
}
