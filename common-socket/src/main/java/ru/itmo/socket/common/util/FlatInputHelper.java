package ru.itmo.socket.common.util;

import ru.itmo.socket.common.data.Coordinates;
import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.common.data.House;
import ru.itmo.socket.common.data.View;
import ru.itmo.socket.common.dto.UserDto;

import java.util.Scanner;

public class FlatInputHelper {
    private static final Scanner console = new Scanner(System.in);

    public static UserDto readUser(Scanner scanner) {
        UserDto userDto = new UserDto();
        userDto.setLogin(inputLogin(scanner));
        userDto.setPassword(inputPassword(scanner));
        return userDto;

    }

    private static String inputLogin(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите login");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Введено пустое значение");
                }
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    private static String inputPassword(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите password");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Введено пустое значение");
                }
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }


    public static Flat readFlat() {
        return readFlat(false);
    }

    // Основной публичный метод, который собирает все части объекта Flat.
    public static Flat readFlat(boolean update) {

        Flat flat = new Flat();

        if (update) {
            flat.setId(inputId());
        }
        if (inputIsGenerateDefault()) {
            Flat generated = Flat.generateDefault();
            generated.setId(flat.getId());
            return generated;
        }
        return manualInput(flat);
    }

    private static boolean inputIsGenerateDefault() {
        System.out.println("Хотите сгенерировать сущность (а не вводить вручную)? (y/n)");
        String input = console.nextLine().trim();

        return !input.equalsIgnoreCase("n");
    }

    private static Flat manualInput(Flat flat) {
        System.out.println("Добавление новой квартиры");
        name(flat);
        coor(flat);
        area(flat);
        rooms(flat);
        bathrooms( flat);
        clHeating(flat);
        view(flat);
        house(flat);
        return flat;
    }

    private static long inputId() {
        while (true) {
            try {
                System.out.println("Введите id");
                String input = console.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }
    protected static void name(Flat flat){
        while (true) {
            System.out.print("Введите имя: ");
            String name = console.nextLine().trim();
            if (!name.isEmpty()) {
                flat.setName(name);
                break;
            }
            System.out.println("Ошибка! Название не может быть пустым.");
        }
    }

    protected static void coor(Flat flat) {
        System.out.println("Введите координаты");

        float x;
        while (true) {
            System.out.print("Введите x (float): ");
            String inputX = console.nextLine().trim();

            if (inputX.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым. Введите число.");
                continue;
            }

            try {
                x = Float.parseFloat(inputX);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат! Введите дробное число!");
            }
        }

        float y;
        while (true) {
            System.out.print("Введите y (float, не более 685): ");
            String inputY = console.nextLine().trim();

            if (inputY.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым. Введите число.");
                continue;
            }

            try {
                y = Float.parseFloat(inputY);
                if (y <= 685) {
                    flat.setCoordinates(new Coordinates(x, y));
                    break;
                } else {
                    System.out.println("Ошибка! Значение не должно превышать 685!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат! Введите дробное число!");
            }
        }
    }


    protected static void area(Flat flat) {
        while (true) {
            System.out.print("Введите площадь квартиры (целое число > 0): ");
            String input = console.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым.");
                continue;
            }
            try {
                double area = Double.parseDouble(input);
                if (area > 0) {
                    flat.setArea(area);
                    break;
                } else {
                    System.out.println("Ошибка! Значение должно быть больше 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }


    protected static void rooms(Flat flat) {
        while (true) {
            System.out.print("Введите количество комнат (целое число > 0): ");
            String input = console.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым.");
                continue;
            }

            try {
                long numberOfRooms = Long.parseLong(input);
                if (numberOfRooms > 0) {
                    flat.setNumberOfRooms(numberOfRooms);
                    break;
                } else {
                    System.out.println("Ошибка! Значение должно быть больше 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }


    protected static void bathrooms(Flat flat) {
        while (true) {
            System.out.print("Введите количество ванных комнат (целое число > 0): ");
            String input = console.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым.");
                continue;
            }

            try {
                int numberOfBathrooms = Integer.parseInt(input);
                if (numberOfBathrooms > 0) {
                    flat.setNumberOfBathrooms(numberOfBathrooms);
                    break;
                } else {
                    System.out.println("Ошибка! Значение должно быть больше 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }


    protected static void clHeating(Flat flat){
        System.out.print("Введите 'да' если есть центральное отопление, и введите 'нет' если нет: ");
        boolean centralHeating = false;
        while (true) {
            String input = console.nextLine().trim().toLowerCase(); //убираем пробелы и сводим к нижнему регистру
            if (input.equals("да")) {
                centralHeating = true;
                break;
            } else if (input.equals("нет")) {
                break;
            } else {
                System.out.println("Некорректный ввод! Введите ответ ещё раз");
            }
        }
        flat.setCentralHeating(centralHeating);
    }

    protected static void view(Flat flat) {
        System.out.println("Введите один из вариантов вида из окна. Варианты: YARD, BAD, NORMAL, GOOD.");
        while (true) {
            System.out.print("Ваш выбор: ");
            String input = console.nextLine().trim().toUpperCase();//убираем пробелы и сводим к верхнему регистру
            try {
                View view = View.valueOf(input);
                flat.setView(view);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Такого варианта нет в списке(YARD, BAD, NORMAL, GOOD)");
            }
        }
    }

    protected static void house(Flat flat) {
        System.out.println("Введите параметры дома");

        // Название дома (строка, не пустая и не пробел)
        String nameHouse;
        while (true) {
            System.out.print("Введите название дома: ");
            nameHouse = console.nextLine().trim();
            if (!nameHouse.isEmpty()) {
                break;
            }
            System.out.println("Ошибка! Название не может быть пустым или состоять только из пробелов.");
        }

        // Остальные параметры (числа > 0)
        int year = getPositiveInt("Введите возраст дома: ");
        long numberOfFloors = getPositiveLong("Введите количество этажей: ");
        long numberOfFlatsOnFloor = getPositiveLong("Введите количество квартир на этаже: ");
        long numberOfLifts = getPositiveLong("Введите количество лифтов: ");

        flat.setHouse(new House(nameHouse, year, numberOfFloors, numberOfFlatsOnFloor, numberOfLifts));
    }



    protected static int getPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = console.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
                else System.out.println("Ошибка! Значение должно быть больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }

    protected static long getPositiveLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = console.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка! Поле не может быть пустым.");
                continue;
            }

            try {
                long value = Long.parseLong(input);
                if (value > 0) return value;
                else System.out.println("Ошибка! Значение должно быть больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }
}
