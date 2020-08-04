package indi.learn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static indi.learn.RandomGenerators.show;

class Bubble {
    public final int i;

    public Bubble(int n) {
        i = n;
    }

    @Override
    public String toString() {
        return "Bubble(" + i + ")";
    }

    /**
     * 纪录对象创建次数
     */
    private static int count = 0;

    /**
     * 静态生成器 Static generator
     *
     * @return 对象（创建次数）
     */
    public static Bubble bubbler() {
        return new Bubble(count++);
    }
}

class RandomGenerators {
    /**
     * 为了消除冗余代码，创建了一个泛型方法来
     * @param stream 流
     * @param <T>   类型参数 T 可以是任何类型，所以这个方法对 Integer、Long 和 Double 类型都生效。
     *              但是 Random 类只能生成基本类型 int， long， double 的流。
     *              幸运的是， boxed()流操作将会自动地把基本类型包装成为对应的装箱类型，
     *              从而使得 show() 能够接受流。
     */
    public static <T> void show(Stream<T> stream) {
        stream
                .limit(4)
                .forEach(System.out::println);
        System.out.println("+++++++++++++++++++");
    }
}

/**
 * 可以使用 Random 为任意对象集合创建 Supplier
 */
class RandomWords implements Supplier<String> {
    List<String> words = new ArrayList<>();
    Random rand = new Random(47);
    RandomWords(String fname) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fname));
        // 略过第一行
        for (String line : lines.subList(1, lines.size())) { // 略过第一行
            for (String word : line.split("[ .?,]+"))
                words.add(word.toLowerCase());
        }
    }
    // 覆盖java.util.function.Supplier中的抽象方法get()
    public String get() {
        return words.get(rand.nextInt(words.size()));
    }
    @Override
    public String toString() {
        return words.stream()
                .collect(Collectors.joining(" "));
    }
}

/**
 * 实用小功能，repeat 代替 简单循环（数字不参与运算，仅仅用来控制循环次数）
 */
class Repeat {
    public static void repeat(int n, Runnable action) {
        IntStream.range(0, n).forEach(i -> action.run());
    }
}

/**
 * <p>
 * 流操作的类型有三种：
 * 创建流，
 * 修改流元素（中间操作， Intermediate Operations），
 * 消费流元素（终端操作， Terminal Operations）。
 * </p>
 * <p>最后一种类型通常意味着收集流元素（通常是到集合中）</p>
 */
public class Streams {
    public static void main(String[] args) throws Exception {
        // 流的创建
        // 1. Stream.of()
        System.out.println(">>> 1. Stream.of() 来创建流:");
        Stream.of(new Bubble(1), new Bubble(2))
                .forEach(System.out::println);
        Stream.of("It's ", "a ", "wonderful ", "day ", "for ", "Stream ", "Learning!")
                .forEach(System.out::print);
        System.out.println();
        Stream.of(3.14159, 2.718, 1.618)
                .forEach(System.out::println);

        // 2. 集合调用 stream()
        // map() 会获取流中的所有元素，并且对流中元素应用操作从而产生新的元素，并将其传递到后续的流中。
        // 通常 map() 会获取对象并产生新的对象，但在这里产生了特殊的用于数值类型的流。
        // 例如，mapToInt() 方法将一个对象流（object stream）转换成为包含整型数字的 IntStream。
        System.out.println("\n>>> 2. 每个集合都可以通过调用 stream() 方法来产生一个流： ");
        List<Bubble> bubbles = Arrays.asList(new Bubble(1), new Bubble(2), new Bubble(3));
        System.out.println(bubbles.stream()
                .mapToInt(b -> b.i)
                .sum());
        Set<String> word = new HashSet<>(Arrays.asList("It's a wonderful day for Learning!".split(" ")));
        word.stream()
                .map(x -> x + " ")
                .forEach(System.out::print);
        System.out.println();
        Map<String, Double> mapNum = new HashMap<>();
        mapNum.put("pi", 3.14159);
        mapNum.put("e", 2.718);
        mapNum.put("phi", 1.618);
        mapNum.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())  // 中间操作 map() 会获取流中的所有元素
                .forEach(System.out::println);

        // 3.随机数流
        System.out.println("\n>>>3. Random 类被一组生成流的方法增强了。");
        Random rand = new Random(47);
        System.out.println("~ 三种类型的随机数流：");
        show(rand.ints().boxed());
        show(rand.ints().boxed());
        show(rand.longs().boxed());
        show(rand.doubles().boxed());
        System.out.println("~ 控制上限和下限：");
        show(rand.ints(10, 20).boxed());
        show(rand.longs(50, 100).boxed());
        show(rand.doubles(20, 30).boxed());
        System.out.println("~ 控制流的大小：");
        show(rand.ints(2).boxed());
        show(rand.longs(2).boxed());
        show(rand.doubles(2).boxed());
        System.out.println("~ 控制流的大小和界限");
        show(rand.ints(3, 3, 9).boxed());
        show(rand.longs(3, 12, 22).boxed());
        show(rand.doubles(3, 11.5, 12.3).boxed());

        // 4.可以使用 Random 为任意对象集合创建 Supplier
        System.out.println("\n>>> 4.可以使用 Random 为任意对象集合创建 Supplier");
        System.out.println(
                // Stream.generate()  它可以把任意 Supplier<T> 用于生成 T 类型的流。
                Stream.generate(new RandomWords("E:\\Learning_java\\indi\\learn\\Cheese.dat"))
                        .limit(10)
                        // collect() 它根据参数来组合所有流中的元素。
                        // Collectors.joining()，你将会得到一个 String 类型的结果，每个元素都根据 joining() 的参数来进行分割。
                        // 还有许多不同的 Collectors 用于产生不同的结果。
                        .collect(Collectors.joining(" ")));

        // 5. int 类型的范围
        System.out.println("\n>>> 5. IntStream 类提供了 range() 方法用于生成整型序列的流。");
        System.out.println("~ 传统方法：");
        for (int i = 1; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println("\n~ for-in 循环：");
        for (int i : IntStream.range(1, 10).toArray()){     // range() 创建了流并将其转化为数组
            System.out.print(i + " ");
        }
        System.out.println("\n~ 流式：");
        IntStream.range(1, 10).forEach(System.out::print); // 全程使用流

        // 6.实用小功能 可以封装流式循环到 自定义的repeat()中 ， 可以用来替换简单的 for 循环。
        // 适用于： 当参数不参与运算，仅仅是用来控制循环次数的时候
        System.out.println("\n>>> 6. 实用小功能 repeat() 可以用来替换简单的 for 循环");
        Repeat.repeat(3, () -> System.out.print("6 "));

        // 7. generate()


    }
}