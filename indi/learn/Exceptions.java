package indi.learn;

/**
 * 自定义异常
 */
class SimpleException extends Exception {}

/**
 * 测试 自定义异常
 */
class InheritingExceptions {
    public void f() throws SimpleException {
        System.out.println("Throw SimpleException from f()");
        throw new SimpleException();
    }
}

/**
 * 可以为异常类创建一个接受字符串参数的构造器
 */
class MyException extends Exception {
    MyException() {}
    MyException(String msg) { super(msg); }
}

public class Exceptions {
    public static void main(String[] args) {
        /**
         * 自定义异常
         */
        InheritingExceptions sed = new InheritingExceptions();
        System.out.println(">>> 体验自定义异常：");
        try {
            sed.f();
        } catch(SimpleException e) {
            System.out.println("Caught it!");
            e.printStackTrace();
        }

    }
}

