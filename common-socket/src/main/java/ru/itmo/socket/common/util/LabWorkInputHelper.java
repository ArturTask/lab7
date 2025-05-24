package ru.itmo.socket.common.util;

import ru.itmo.socket.common.entity.*;

import java.time.ZonedDateTime;
import java.util.Scanner;

public class LabWorkInputHelper {

    public static LabWork readLabWork(Scanner scanner) {
        return readLabWork(scanner, false);
    }

    // Основной публичный метод, который собирает все части объекта LabWork.
    public static LabWork readLabWork(Scanner scanner, boolean update) {

        LabWork labWork = new LabWork();

        if (update) {
            labWork.setId(inputId(scanner));
        }
        labWork.setName(inputName(scanner));
        labWork.setCoordinates(inputCoordinates(scanner));
        labWork.setMinimalPoint(inputMinimalPoint(scanner));
        labWork.setDifficulty(inputDifficulty(scanner));
        labWork.setAuthor(inputAuthor(scanner));

        return labWork;
    }

    private static long inputId(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите id");
                String input = scanner.nextLine().trim();

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

    // Чтение названия LabWork.
    public static String inputName(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите название:");
                String name = scanner.nextLine().trim();
                if (name.isEmpty() || name.equals("null")) {
                    throw new IllegalArgumentException("Название не может быть пустым");
                }
                return name;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координат.
    public static Coordinates inputCoordinates(Scanner scanner) {
        Coordinates coordinates = new Coordinates(0, 0);
        coordinates.setX(inputCoordinateX(scanner));
        coordinates.setY(inputCoordinateY(scanner));
        return coordinates;
    }

    // Чтение координаты X.
    private static float inputCoordinateX(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите координату X (float, макс 879):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                float x = Float.parseFloat(input);

                if (x > 879) {
                    throw new IllegalArgumentException("Координата X должна быть не больше 879");
                }

                return x;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координаты Y.
    private static float inputCoordinateY(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите координату Y:");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                float y = Float.parseFloat(input);
                return y;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение минимального балла.
    public static long inputMinimalPoint(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите минимальный балл (целое число > 0):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Минимальный балл не может быть пустым или равным null");
                }

                long value = Long.parseLong(input);
                if (value <= 0) {
                    throw new IllegalArgumentException("Минимальный балл должен быть больше 0");
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение сложности.
    public static Difficulty inputDifficulty(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите сложность (NORMAL, IMPOSSIBLE, INSANE, HOPELESS):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    return null;
                }

                try {
                    return Difficulty.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Неверное значение сложности. Введите одно из следующих: NORMAL, IMPOSSIBLE, INSANE, HOPELESS");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }



    // Чтение информации об авторе.
    public static Person inputAuthor(Scanner scanner) {
        Person author = new Person("temporaryName", ZonedDateTime.now(), 1f, 1f, Color.BROWN);
        author.setName(inputAuthorName(scanner));
        author.setBirthday(inputAuthorBirthday(scanner));
        author.setHeight(inputAuthorHeight(scanner));
        author.setWeight(inputAuthorWeight(scanner));
        author.setEyeColor(inputAuthorEyeColor(scanner));
        return author;
    }

    // Чтение имени автора.
    public static String inputAuthorName(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите имя автора:");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()|| name.equals("null")) {
                    throw new IllegalArgumentException("Имя автора не может быть пустым или равен null");
                }
                return name;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение дня рождения автора.
    public static ZonedDateTime inputAuthorBirthday(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите день рождения автора (гггг-мм-дд):");
                String dateInput = scanner.nextLine().trim();

                if (dateInput.isEmpty() || dateInput.equalsIgnoreCase("null")) {
                    return null;
                }

                return ZonedDateTime.parse(dateInput + "T00:00:00+03:00[Europe/Moscow]");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение роста автора.
    public static float inputAuthorHeight(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите рост автора (float > 0):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Рост автора не может быть пустым или равным null");
                }

                float height = Float.parseFloat(input);
                if (height <= 0) {
                    throw new IllegalArgumentException("Рост автора должен быть больше 0");
                }
                return height;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение веса автора.
    public static float inputAuthorWeight(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите вес автора (float > 0):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Вес автора не может быть пустым или равным null");
                }

                float weight = Float.parseFloat(input);
                if (weight <= 0) {
                    throw new IllegalArgumentException("Вес автора должен быть больше 0");
                }
                return weight;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение цвета глаз автора.
    public static Color inputAuthorEyeColor(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите цвет глаз (GREEN, RED, BROWN):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Цвет глаз не может быть пустым или равным null");
                }

                try {
                    return Color.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Неверное значение цвета глаз. Введите одно из следующих: GREEN, RED, BROWN");
                }

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }
}
