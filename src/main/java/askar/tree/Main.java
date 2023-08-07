package askar.tree;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // - Создать категорию [1]
        // - Переместить категорию [2]
        // - Удалить категорию [3]
        // Выберите действие: ___

        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите действие!");
        System.out.println("- Создать категорию [1]");
        System.out.println("- Переместить категорию [2]");
        System.out.println("- Удалить категорию [3]");

        int input = scanner.nextInt();

        if (input == 1) {
            Create.createCategory();
        } else if (input == 2) {
            Move.moveCategory();
        } else if (input == 3) {
            Delete.deleteCategory();
        }


    }
}
